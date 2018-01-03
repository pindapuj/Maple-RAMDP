package rocksample;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import rocksample.state.RockSampleState;
import rocksample.state.RoverAgent;

/**
 * Created by steph on 10/26/2017.
 * TODO: maybe not have hardcoded value of '4' OR have opportunity to pass in the zone that is the terminal zone
 */
public class RockSampleTerminalFunction implements TerminalFunction {

    // isTerminal
    // Given a state, returns whether or not the state is terminal
    @Override
    public boolean isTerminal(State s){
        RockSampleState state = (RockSampleState) s;
        RoverAgent rover = state.getRover();

        int roverX = (int) rover.get(RockSample.ATT_X);

        // if the rover is in the terminal zone
        if (roverX == 4) {
            return true;
        }
        return false;
    }
}
