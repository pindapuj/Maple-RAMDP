package taxi;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import taxi.state.RoverAgent;
import taxi.state.RockSampleState;

public class RockSampleTerminalFunction implements TerminalFunction{
	//the taxi domain is terminal when all passengers are at their goal
	//and have been picked up and not in the taxi anymore
	
	@Override

	public boolean isTerminal(State s) {
		RockSampleState state = (RockSampleState) s;
		RoverAgent rover = state.getTaxi();
		int roverX = (int) rover.get(Taxi.ATT_X);
		int roverY = (int) rover.get(Taxi.ATT_Y);

		if(roverX == 4 && roverY == 4){
			return true;
		}
		return false;
		/*
		String [] passengerName = state.getPassengers();
		String passengerGoal = (String) state.getPassengerAtt(passengerName[0], Taxi.ATT_GOAL_LOCATION);
		//String passengerGoal = (String) Taxi.
		int px = (int) TaxiState.getTaxi.get(Taxi.ATT_X);
		int py = (int) Taxi.get(Taxi.ATT_Y);


		for(String locName : state.getLocations()){
			if(passengerGoal.equals(locName)){
				int lx = (int) state.getLocationAtt(locName, Taxi.ATT_X);
				int ly = (int) state.getLocationAtt(locName, Taxi.ATT_Y);
				if(lx != px || ly != py)
					return false;
		}

		for(String passengerName : state.getPassengers()){
			boolean inTaxi = (boolean) state.getPassengerAtt(passengerName, Taxi.ATT_IN_TAXI);
			if(inTaxi)
				return false;
			
			String passengerGoal = (String) state.getPassengerAtt(passengerName, Taxi.ATT_GOAL_LOCATION);
			int px = (int) state.getPassengerAtt(passengerName, Taxi.ATT_X);
			int py = (int) state.getPassengerAtt(passengerName, Taxi.ATT_Y);
			
			for(String locName : state.getLocations()){
				if(passengerGoal.equals(locName)){
					int lx = (int) state.getLocationAtt(locName, Taxi.ATT_X);
					int ly = (int) state.getLocationAtt(locName, Taxi.ATT_Y);
					if(lx != px || ly != py)
						return false;
					
					break;
				}
			}
		} *

		return true; */
	}
}
