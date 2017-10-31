package rocksample;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import rocksample.state.RockSampleState;
import rocksample.state.RoverAgent;

/**
 * Created by steph on 10/26/2017.
 */
public class RockSampleTerminalFunction implements TerminalFunction {
    @Override

    public boolean isTerminal(State s) {
        RockSampleState state = (RockSampleState) s;
        RoverAgent rover = state.getRover();
        int roverX = (int) rover.get(RockSample.ATT_X);

        if (roverX == 4) {
            return true;
        }
        return false;
    }
}
