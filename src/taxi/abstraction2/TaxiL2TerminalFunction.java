package taxi.abstraction2;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import taxi.abstraction2.state.TaxiL2State;

public class TaxiL2TerminalFunction implements TerminalFunction {

	@Override
	public boolean isTerminal(State s) {
		TaxiL2State state = (TaxiL2State) s;
		
		for(String passengerName : state.getPassengers()){
			String location = (String) state.getPassengerAtt(passengerName, TaxiL2.ATT_CURRENT_LOCATION);
			String goalLocation = (String) state.getPassengerAtt(passengerName, TaxiL2.ATT_GOAL_LOCATION);
			if(!location.equals(goalLocation))
				return false;
			
			boolean inTaxi = (boolean) state.getPassengerAtt(passengerName, TaxiL2.ATT_IN_TAXI);
			boolean pickedUp = (boolean) state.getPassengerAtt(passengerName, TaxiL2.ATT_PICKED_UP_AT_LEAST_ONCE);
			if(inTaxi || !pickedUp)
				return false;
		}
		return true;
	}

}