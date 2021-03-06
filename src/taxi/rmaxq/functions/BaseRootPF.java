package taxi.rmaxq.functions;

import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import taxi.Taxi;
import taxi.state.RockSampleState;

public class BaseRootPF  extends PropositionalFunction{
	//root is complet when all pasengers are at their goal and not in taxi

	public BaseRootPF (){
		super("root", new String[]{});
	}

	@Override
	public boolean isTrue(OOState s, String... params) {
		RockSampleState state = (RockSampleState) s;

		for(String passengerName : state.getPassengers()) {
			boolean inTaxi = (boolean) state.getPassengerAtt(passengerName, Taxi.ATT_IN_TAXI);
//			boolean pickedUpOnce = (boolean) state.getPassengerAtt(passengerName,
//					Taxi.ATT_PICKED_UP_AT_LEAST_ONCE);
			if (inTaxi )
				return false;

			String passengerGoal = (String) state.getPassengerAtt(passengerName, Taxi.ATT_GOAL_LOCATION);
			int px = (int) state.getPassengerAtt(passengerName, Taxi.ATT_X);
			int py = (int) state.getPassengerAtt(passengerName, Taxi.ATT_Y);

			for (String locName : state.getLocations()) {
				if (passengerGoal.equals(locName)) {
					int lx = (int) state.getLocationAtt(locName, Taxi.ATT_X);
					int ly = (int) state.getLocationAtt(locName, Taxi.ATT_Y);
					if (lx != px || ly != py)
						return false;

					break;
				}
			}
		}
		return true;
	}
}
