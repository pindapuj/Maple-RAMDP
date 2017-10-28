package rocksample;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;

import org.apache.commons.lang3.ObjectUtils;
import rocksample.state.RockSampleRock;
import rocksample.state.RockSampleState;
import rocksample.state.RoverAgent;
/**
 * Created by steph on 10/26/2017.
 */
public class RockSampleRewardFunction implements RewardFunction {

    private double exitAreaReward;
    private double goodRockReward;
    private double badRockReward;

    private TerminalFunction tf;

    public RockSampleRewardFunction(){
        exitAreaReward = 10;
        goodRockReward = 10;
        badRockReward = -10;
        tf = new RockSampleTerminalFunction();
    }

    public double reward(State s, Action a, State sprime) {
        RockSampleState state = (RockSampleState) sprime;
        RoverAgent rover = state.getRover();
        int roverX = (int) rover.get(RockSample.ATT_X);
        int roverY = (int) rover.get(RockSample.ATT_Y);

        if(a.actionName().equals(RockSample.ACTION_SAMPLE)){
            RockSampleRock rock = state.getRockAtPoint(roverX, roverY);

            if (rock != null) {
                if (rock.get(RockSample.ATT_QUALITY) == "Good") {
                    return goodRockReward;
                } else if (rock.get(RockSample.ATT_QUALITY) == "Bad") {
                    return badRockReward;
                } else {
                    return 0;
                }
            }
        }

        if (roverX == 4) {
            return exitAreaReward;
        }
        return 0;
    }
}
