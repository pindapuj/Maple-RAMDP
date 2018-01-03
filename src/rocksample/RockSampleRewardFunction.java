package rocksample;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;

import rocksample.state.RockSampleRock;
import rocksample.state.RockSampleState;
import rocksample.state.RoverAgent;

/**
 * Created by steph on 10/26/2017.
 */

// TODO: Change hardcoded terminal area value in reward()
public class RockSampleRewardFunction implements RewardFunction {

    private double exitAreaReward;
    private double goodRockReward;
    private double badRockReward;

    private TerminalFunction tf;

    public RockSampleRewardFunction() {
        exitAreaReward = 10;
        goodRockReward = 10;
        badRockReward = -10;
        tf = new RockSampleTerminalFunction();
    }

    // reward
    // Given a state, action, and subsequent state, return the corresponding reward
    public double reward(State s, Action a, State sprime) {
        RockSampleState state = (RockSampleState) sprime;
        RoverAgent rover = state.getRover();
        int roverX = (int) rover.get(RockSample.ATT_X);
        int roverY = (int) rover.get(RockSample.ATT_Y);

        // reward for sampling rock
        if(a.actionName().equals(RockSample.ACTION_SAMPLE)) {
            RockSampleState prevstate = (RockSampleState) s;
            RockSampleRock rock = prevstate.getRockAtPoint(roverX, roverY);

            // if the rock exists
            if (rock != null) {
                if (rock.get(RockSample.ATT_QUALITY) == "Good") {
                    return goodRockReward;
                } else {
                    return badRockReward;
                }
            }
        }

        // if the rover is in the terminal zone
        if (roverX == 4) {
            return exitAreaReward;
        }
        return 0;
    }
}
