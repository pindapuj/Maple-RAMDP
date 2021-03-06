package taxi.hierarchies.tasks.dropoff.state;

import java.util.ArrayList;
import java.util.List;

import burlap.mdp.auxiliary.StateMapping;
import burlap.mdp.core.state.State;
import taxi.Taxi;
import taxi.hierarchies.tasks.dropoff.TaxiDropoffDomain;
import taxi.state.RockSampleState;

public class DropoffStateMapper implements StateMapping {
	//projection function from the base taxi to abstraction 1
	
	@Override
	public State mapState(State s) {
		List<TaxiDropoffPassenger> passengers = new ArrayList<TaxiDropoffPassenger>();
		RockSampleState st = (RockSampleState) s;

		// Get Passengers
		for(String passengerName : st.getPassengers()){
			int px = (int) st.getPassengerAtt(passengerName, Taxi.ATT_X);
			int py = (int) st.getPassengerAtt(passengerName, Taxi.ATT_Y);
			boolean inTaxi = (boolean) st.getPassengerAtt(passengerName, Taxi.ATT_IN_TAXI);
			String passengerLocation = TaxiDropoffDomain.NOT_IN_TAXI;

			if(inTaxi) {
				for (String locName : st.getLocations()) {
					int lx = (int) st.getLocationAtt(locName, Taxi.ATT_X);
					int ly = (int) st.getLocationAtt(locName, Taxi.ATT_Y);

					if (px == lx && py == ly) {
						passengerLocation = locName;
						break;
					}
				}
			}
			passengers.add(new TaxiDropoffPassenger(passengerName, passengerLocation));
		}
		return new TaxiDropoffState(passengers);
	}

}
