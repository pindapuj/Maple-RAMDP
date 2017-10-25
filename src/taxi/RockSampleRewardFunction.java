package taxi;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;
import taxi.state.RoverAgent;
import taxi.state.RockSampleState;

public class RockSampleRewardFunction implements RewardFunction{

	/**
	 * the reward for taking a action
	 */
	private double stepReward;
	
	/**
	 * the reward for a impossible pickup or dropoff action
	 */
	private double illegalActionReward;
	
	/**
	 * the reward for completing the goal
	 */
	private double goalReward;
	private double goodRockReward;
	private double badRockReward;
	private double exitAreaReward;
	
	/**
	 * the taxi terminal function
	 */
	private TerminalFunction tf;
	
	/**
	 * use the default rewards
	 */
	public RockSampleRewardFunction() {
		goodRockReward = 10;
		badRockReward = -10;
		exitAreaReward = 10;
		//stepReward = -1;
		//illegalActionReward = -10;
		//goalReward = 20;
		tf = new RockSampleTerminalFunction();
	}
	
	/**
	 * use custom rewards
	 * @param stepR the reward for a action
	 * @param illegalR the reward for a impossible pickup or dropoff
	 * @param goalR the reward for completing the goal
	 */
	public RockSampleRewardFunction(double stepR, double illegalR, double goalR){
		stepReward = stepR;
		illegalActionReward = illegalR;
		goalReward = goalR;
		tf = new RockSampleTerminalFunction();
	}
	
	@Override
	public double reward(State s, Action a, State sprime) {
		//TaxiState state = (TaxiState) s;


		RockSampleState state = (RockSampleState) sprime;
		RoverAgent rover = state.getTaxi();
		int roverX = (int) rover.get(Taxi.ATT_X);
		int roverY = (int) rover.get(Taxi.ATT_Y);

		if(roverX == 4 && roverY == 4){
			return exitAreaReward;
		}
		return 0;
		/*
		//goal reward when state is terminal
		if(tf.isTerminal(sprime))
			return goalReward + stepReward;
		
		boolean taxiOccupied = (boolean) state.getTaxiAtt(Taxi.ATT_TAXI_OCCUPIED);
		int tx = (int) state.getTaxiAtt(Taxi.ATT_X);
		int ty = (int) state.getTaxiAtt(Taxi.ATT_Y);
		
		//illegal pickup when no passenger at taxi's location
		if(a.actionName().equals(Taxi.ACTION_PICKUP)){

			boolean passengerAtTaxi = false;
			for(String passengerName : state.getPassengers()){
				int px = (int) state.getPassengerAtt(passengerName, Taxi.ATT_X);
				int py = (int) state.getPassengerAtt(passengerName, Taxi.ATT_Y);
				if(px == tx && py == ty){
					passengerAtTaxi = true;
					break;
				}
			}
			
			if(!passengerAtTaxi)
				return stepReward + illegalActionReward;
		}
		//illegal dropoff if not at depot or passenger not in taxi
		else if(a.actionName().startsWith(Taxi.ACTION_PUTDOWN)){
			if(!taxiOccupied)
				return stepReward + illegalActionReward;
			
			// if taxi/passenger is not at depot
			boolean taxiAtDepot = false;
			for(String locName : state.getLocations()){
				int lx = (int) state.getLocationAtt(locName, Taxi.ATT_X);
				int ly = (int) state.getLocationAtt(locName, Taxi.ATT_Y);
				if(tx == lx && ty == ly){
					taxiAtDepot = true;
					break;
				}
			}
			
			if(!taxiAtDepot)
				return stepReward + illegalActionReward;
		}
		
		return stepReward; */
	}
}
