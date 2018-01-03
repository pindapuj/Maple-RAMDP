package rocksample;

import burlap.debugtools.RandomFactory;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;
import rocksample.state.RockSampleRock;
import rocksample.state.RockSampleState;
import rocksample.state.RoverAgent;

import java.util.ArrayList;
import java.util.List;

public class RockSampleModel implements FullStateModel {

    /**
     * the array saying how the probabilities are distributed
     */
    private double[][] moveProbability;

    private boolean noisy;

    private double noisyProbability;

    // RockSampleModel
    // Given the movement probability, whether or not the sensor is noisy, and the noisy probability of the sensor,
    // creates a new RockSampleModel
    public RockSampleModel(double[][] moveprob, boolean noisy, double noisyProb) {
        this.moveProbability = moveprob;
        this.noisyProbability = noisyProb;
        this.noisy = noisy;
    }

    // RockSampleModel
    // Given the movement probability, creates a new RockSampleModel
    public RockSampleModel(double[][] moveprob) {
        this.moveProbability = moveprob;
        this.noisy = false;
    }

    // sample
    // Given a state and an action, returns the next state
    @Override
    public State sample(State s, Action a) {
        List<StateTransitionProb> stpList = this.stateTransitions(s,a);

        // get a random number
        double roll = RandomFactory.getMapped(0).nextDouble();
        System.out.println(roll);
        double curSum = 0.;

        // sum up state trans prob
        for(int i = 0; i < stpList.size(); i++) {
            curSum += stpList.get(i).p;
            if(roll < curSum){
                // return the corresponding state
                return stpList.get(i).s;
            }
        }
        throw new RuntimeException("Probabilities don't sum to 1.0: " + curSum);
    }

    // stateTransitions
    // Given a state and an action, returns all of the state transition probabilities from that state-action pair
    @Override
    public List<StateTransitionProb> stateTransitions(State s, Action a) {
        List<StateTransitionProb> tps = new ArrayList<StateTransitionProb>();
        int action = actionInd(a);
        RockSampleState rockSampleS = (RockSampleState) s;

        if (action <= RockSample.IND_WEST) {
            movement(rockSampleS, action, tps);
        } else if (action == RockSample.IND_SAMPLE) {
            sampleRock(rockSampleS, action, tps);
        } else if (action == RockSample.IND_CHECK) {
            checkRock(rockSampleS, (ObjectParameterizedAction) a, tps);
        }
        return tps;
    }

    // movement
    // Given a state, an action index, and the state transition probabilities, moves the rover if the action is a
    // viable movement action
    public void movement(RockSampleState s, int action, List<StateTransitionProb> tps) {
        double[] moveProbabilities = this.moveProbability[action];

        int roverX = (int) s.getRoverAtt(RockSample.ATT_X);
        int roverY = (int) s.getRoverAtt(RockSample.ATT_Y);

        for (int outcome = 0; outcome < moveProbabilities.length; outcome++) {
            double p = moveProbabilities[outcome];
            if (p == 0)
                continue;

            int dx = 0, dy = 0;
            RockSampleState ns = s.copy();

            // move in the given direction unless there are walls in the way
            if (outcome == RockSample.IND_NORTH) {
                if (!ns.wallNorth()) {
                    dy = +1;
                }
            } else if (outcome == RockSample.IND_EAST) {
                if (!ns.wallEast()) {
                    dx = +1;
                }
            } else if (outcome == RockSample.IND_SOUTH) {
                if (!ns.wallSouth()) {
                    dy = -1;
                }
            } else if (outcome == RockSample.IND_WEST) {
                if (!ns.wallWest()) {
                    dx = -1;
                }
            }

            int nx = roverX + dx;
            int ny = roverY + dy;
            RoverAgent nrover = ns.touchRover();
            nrover.set(RockSample.ATT_X, nx);
            nrover.set(RockSample.ATT_Y, ny);

            tps.add(new StateTransitionProb(ns,p));
        }
    }

    // sampleRock
    // Given a state, an action index, and the state transition probabilities, samples the rock at the agent's location
    public void sampleRock(RockSampleState s, int action, List<StateTransitionProb> tps) {
        RockSampleState ns = s.copy();

        int roverX = (int) s.getRoverAtt(RockSample.ATT_X);
        int roverY = (int) s.getRoverAtt(RockSample.ATT_Y);

        RockSampleRock rock = ns.getRockAtPoint(roverX, roverY);

        // if there is a rock at the agent's location
        if (rock != null) {
            int rx = (int) rock.get(RockSample.ATT_X);
            int ry = (int) rock.get(RockSample.ATT_Y);
            RockSampleRock nRock = ns.touchRock(rock.name());
            nRock.set(RockSample.ATT_QUALITY, "Bad");
        }

        // else?

        tps.add(new StateTransitionProb(ns, 1.));
    }

    // checkRock
    // Given a state, an action, and the state transition probabilities, checks the quality of a the rock that
    // parameterizes the action
    public void checkRock(RockSampleState s, ObjectParameterizedAction a, List<StateTransitionProb> tps) {
        String n = a.getObjectParameters()[0];
        RockSampleState ns = s.copy();

        // get the location of the rock
        int rockX = (int) s.getRockAtt(n, RockSample.ATT_X);
        int rockY = (int) s.getRockAtt(n, RockSample.ATT_Y);

        // get the quality of the rock at that particular location
        String rockQuality = (String) s.getRockAtt(n, RockSample.ATT_QUALITY);

        tps.add(new StateTransitionProb(ns, 1.));
    }

    // actionInd
    // Given an action, returns the index of that action
    public int actionInd(Action a) {
        String aname = a.actionName();
        if(aname.startsWith(RockSample.ACTION_NORTH))
            return RockSample.IND_NORTH;
        else if(aname.startsWith(RockSample.ACTION_EAST))
            return RockSample.IND_EAST;
        else if(aname.startsWith(RockSample.ACTION_SOUTH))
            return RockSample.IND_SOUTH;
        else if(aname.startsWith(RockSample.ACTION_WEST))
            return RockSample.IND_WEST;
        else if(aname.startsWith(RockSample.ACTION_SAMPLE))
            return RockSample.IND_SAMPLE;
        else if(aname.startsWith(RockSample.ACTION_CHECK))
            return RockSample.IND_CHECK;
        throw new RuntimeException("Invalid action " + aname);
    }
}
