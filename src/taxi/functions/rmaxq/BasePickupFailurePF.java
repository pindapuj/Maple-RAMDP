package taxi.functions.rmaxq;

import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import taxi.Taxi;
import taxi.state.RockSampleState;

public class BasePickupFailurePF extends PropositionalFunction {

	public BasePickupFailurePF() {
		super("pickup>L!", new String[]{});
	}

	@Override
	public boolean isTrue(OOState s, String... params) {
		// if the taxi is not at depot or not occupied
		RockSampleState st = (RockSampleState) s;

		int tx = (int) st.getTaxiAtt(Taxi.ATT_X);
		int ty = (int) st.getTaxiAtt(Taxi.ATT_Y);

		for(String passengerName : st.getPassengers()){
			int px = (int) st.getPassengerAtt(passengerName, Taxi.ATT_X);
			int py = (int) st.getPassengerAtt(passengerName, Taxi.ATT_Y);
			if(tx == px && ty == py){
				return (boolean) st.getTaxiAtt(Taxi.ATT_TAXI_OCCUPIED);
			}
		}

		return true;
	}
}
