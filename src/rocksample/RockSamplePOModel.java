package rocksample;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.model.FullModel;
import burlap.mdp.singleagent.model.TransitionProb;

import java.util.Arrays;
import java.util.List;

import rocksample.state.RockSampleRock;
import rocksample.state.RockSampleState;
import rocksample.state.RoverAgent;

import static rocksample.RockSamplePO.*;

/**
 * Created by steph on 10/26/2017.
 */

/** TODO: (in process) changing from fullstatemodel to fullmodel like in tiger
 *
 */
//public class RockSamplePOModel implements FullStateModel {
public class RockSamplePOModel implements FullModel {
    public double exitAreaReward;
    public double goodRockReward;
    public double badRockReward;
    public double noReward;

    public RockSamplePOModel(double exitAreaReward, double goodRockReward, double badRockReward, double noReward) {
        this.exitAreaReward = exitAreaReward;
        this.goodRockReward = goodRockReward;
        this.badRockReward = badRockReward;
        this.noReward = noReward;
    }

    // Not sure if having false for transition makes sense in all cases for movement/ if having noreward
    // makes sense in all cases for movement, but we will see what happens
    @Override
    public List<TransitionProb> transitions(State s, Action a) {
        String actionName = a.actionName();

        // if the action is one of the movement actions
        if (actionName.equals(ACTION_NORTH)
                || actionName.equals(ACTION_SOUTH)
                || actionName.equals(ACTION_EAST)
                || actionName.equals(ACTION_WEST)) {
            return Arrays.asList(
                    new TransitionProb(1., new EnvironmentOutcome(s, a, s, noReward, false)));
        }
        else if (actionName.equals(ACTION_SAMPLE)) {
            State rs = s.copy();
            RockSampleRock rock = getRock((RockSampleState) s);

            // not sure if transition prob should be 0.5 here
            if (rock != null) {
                double reward = rock.get(ATT_QUALITY) == ATT_GOOD ? goodRockReward : badRockReward;
                return Arrays.asList(
                        new TransitionProb(0.5, new EnvironmentOutcome(s, a,
                                new RockSampleState(rock.name(), ATT_GOOD), reward, false)),
                        new TransitionProb(0.5, new EnvironmentOutcome(s, a,
                                new RockSampleState(rock.name(), ATT_BAD), reward, false)));

            }
            else {
                return Arrays.asList(
                        new TransitionProb(1., new EnvironmentOutcome(s, a, s, noReward, false)));
            }
        }
        else if (actionName.equals(ACTION_CHECK)) {
            return Arrays.asList(
                    new TransitionProb(1., new EnvironmentOutcome(s, a, s, noReward, false)));
        }
        throw new RuntimeException("Unknown action " + a.toString());
    }

    public void updateRock(RockSampleState s, RockSampleRock rock) {
        RockSampleRock nRock = s.touchRock(rock.name());
        nRock.set(ATT_QUALITY, ATT_BAD);
    }

    public RockSampleRock getRock(RockSampleState s) {
        int roverX = (int) s.getRoverAtt(ATT_X);
        int roverY = (int) s.getRoverAtt(ATT_Y);

        RockSampleRock rock = s.getRockAtPoint(roverX, roverY);
        return rock;
    }

    @Override
    public EnvironmentOutcome sample(State state, Action action) {
        String actionName = action.actionName();
        // not sure if this is totally right. haven't really worked with envoutcome before
        // might need to include something new in for termination area
        if (actionName.equals(ACTION_NORTH)
                || actionName.equals(ACTION_EAST)
                || actionName.equals(ACTION_SOUTH)
                || actionName.equals(ACTION_WEST)) {
            RockSampleState ns = movement((RockSampleState) state, action);
            return new EnvironmentOutcome(state, action, ns, noReward, false);
        } else if (actionName.equals(ACTION_SAMPLE)) {
            RockSampleRock rock = getRock((RockSampleState) state);
            RockSampleState ns = sampleRock(rock, (RockSampleState) state);

            if (rock == null) {
                return new EnvironmentOutcome(state, action, state, noReward, false);
            } else if (rock.get(ATT_QUALITY) == ATT_GOOD) {
                return new EnvironmentOutcome(state, action, ns, goodRockReward, false);
            } else {
                return new EnvironmentOutcome(state, action, ns, badRockReward, false);
            }

        } else if (actionName.equals(ACTION_CHECK)) {
            return new EnvironmentOutcome(state, action, state, noReward, false);

        }

        throw new RuntimeException("Unknown action " + action.toString());
    }

    public RockSampleState sampleRock(RockSampleRock rock, RockSampleState state) {
        RockSampleState ns = state.copy();

        if (rock != null) {
            updateRock(ns, rock);
        }

        return ns;
    }

    public RockSampleState movement(RockSampleState state, Action action) {
        int roverX = (int) state.getRoverAtt(ATT_X);
        int roverY = (int) state.getRoverAtt(ATT_Y);

        int dx = 0, dy = 0;

        RockSampleState ns = state.copy();

        if (action.equals(ACTION_NORTH)) {
            if (!ns.wallNorth()) {
                dy = +1;
            }
        } else if (action.equals(ACTION_EAST)) {
            if (!ns.wallEast()) {
                dx = +1;
            }
        } else if (action.equals(ACTION_SOUTH)) {
            if (!ns.wallSouth()) {
                dy = -1;
            }
        } else if (action.equals(ACTION_WEST)) {
            if (!ns.wallWest()) {
                dx = -1;
            }
        }

        int nx = roverX + dx;
        int ny = roverY + dy;

        RoverAgent nrover = ns.touchRover();
        nrover.set(ATT_X, nx);
        nrover.set(ATT_Y, ny);

        return ns;
    }

    @Override
    public boolean terminal(State state) {
        return false;
    }
}
