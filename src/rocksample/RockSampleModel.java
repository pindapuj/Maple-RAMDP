package rocksample;

import burlap.debugtools.RandomFactory;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;

import java.util.ArrayList;
import java.util.List;

import rocksample.state.RockSampleRock;
import rocksample.state.RockSampleState;
import rocksample.state.RoverAgent;

/**
 * Created by steph on 10/26/2017.
 */
public class RockSampleModel implements FullStateModel {
    /**
     * the array saying how the probabilities are distributed
     */
    private double[][] moveProbability;

    public RockSampleModel(double[][] moveprob) {
        this.moveProbability = moveprob;
    }

    @Override
    public State sample(State s, Action a) {
        List<StateTransitionProb> stpList = this.stateTransitions(s,a);
        double roll = RandomFactory.getMapped(0).nextDouble();
         System.out.println(roll);
        double curSum = 0.;
        for(int i = 0; i < stpList.size(); i++){
            curSum += stpList.get(i).p;
            if(roll < curSum){
                return stpList.get(i).s;
            }
        }
        throw new RuntimeException("Probabilities don't sum to 1.0: " + curSum);
    }

    @Override
    public List<StateTransitionProb> stateTransitions(State s, Action a) {
        List<StateTransitionProb> tps = new ArrayList<StateTransitionProb>();
        int action = actionInd(a);
        RockSampleState rockSampleS = (RockSampleState) s;

        if(action <= RockSample.IND_WEST){
            movement(rockSampleS, action, tps);
        }else if(action == RockSample.IND_SAMPLE){
            sampleRock(rockSampleS, action, tps);
        }
        return tps;
    }

    public void movement(RockSampleState s, int action, List<StateTransitionProb> tps) {
        double[] moveProbabilities = this.moveProbability[action];

        int tx = (int) s.getRoverAtt(RockSample.ATT_X);
        int ty = (int) s.getRoverAtt(RockSample.ATT_Y);

        for (int outcome = 0; outcome < moveProbabilities.length; outcome++) {
            double p = moveProbabilities[outcome];
            if (p == 0)
                continue;

            int dx = 0, dy = 0;
            RockSampleState ns = s.copy();

            //move in the given direction unless there are walls in the way
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

            int nx = tx + dx;
            int ny = ty + dy;
            RoverAgent nrover = ns.touchRover();
            nrover.set(RockSample.ATT_X, nx);
            nrover.set(RockSample.ATT_Y, ny);

            tps.add(new StateTransitionProb(ns,p));
        }
    }

    public void sampleRock(RockSampleState s, int action, List<StateTransitionProb> tps){
        RockSampleState ns = s.copy();

        int roverX = (int) s.getRoverAtt(RockSample.ATT_X);
        int roverY = (int) s.getRoverAtt(RockSample.ATT_Y);

        RockSampleRock rock = ns.getRockAtPoint(roverX, roverY);
        if (rock != null){
            int rx = (int) rock.get(RockSample.ATT_X);
            int ry = (int) rock.get(RockSample.ATT_Y);
            RockSampleRock nRock = ns.touchRock(rock.name());
            nRock.set(RockSample.ATT_QUALITY, "Bad");

        }

        tps.add(new StateTransitionProb(ns, 1.));
    }

    public int actionInd(Action a){
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
        throw new RuntimeException("Invalid action " + aname);
    }
}
