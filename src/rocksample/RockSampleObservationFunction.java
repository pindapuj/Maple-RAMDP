package rocksample;

import burlap.debugtools.RandomFactory;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.pomdp.observations.DiscreteObservationFunction;
import burlap.mdp.singleagent.pomdp.observations.ObservationProbability;
import burlap.mdp.singleagent.pomdp.observations.ObservationUtilities;
import rocksample.state.RockSampleState;


import java.util.ArrayList;
import java.util.List;

import static rocksample.RockSamplePO.*;

/**
 * Created by steph on 11/9/2017.
 */
/**TODO: Is the explicit addition of all possible states excessive? Is there an easier way we can do this to make
   more extensible?
 */
public class RockSampleObservationFunction implements DiscreteObservationFunction {

    protected double sensorAccuracy;

    public RockSampleObservationFunction(double sensorAccuracy) {
        this.sensorAccuracy = sensorAccuracy;
    }

    //@Override
    public boolean canEnumerateObservation() {
        return true;
    }

    // think this is maybe more like observation good rock observation bad rock rather than the observation of the
    // entire state with all of the possibilities enumerated. each observation does not include all of the rocks, but
    // should be specifically updating a particular rock - if that makes sense.
    public List<State> allObservations() {

        List<State> result = new ArrayList<State>(NUM_ROCKS * 2);

        for(int i = 0; i < NUM_ROCKS; i++) {
            result.add(this.observationGoodRock(CLASS_ROCK + i));
            result.add(this.observationBadRock(CLASS_ROCK + i));
        }

        return result;
    }

    protected State observationGoodRock(String name) {
        return new RockSampleState(name, ATT_GOOD);
    }

    protected State observationBadRock(String name) {
        return new RockSampleState(name, ATT_BAD);
    }

    public void setSensorAccuracy(int roverX, int roverY, int rockX, int rockY) {
        int dx = (roverX - rockX) * (roverX - rockX); //squaring
        int dy = (roverY - rockY) * (roverY - rockY); //square

        double distance = Math.sqrt(dx+ dy);
        double tunable_constant = 20;

        double sensorAccuracy = Math.pow(2, -(distance)/tunable_constant);

        this.sensorAccuracy = sensorAccuracy;
    }

    @Override
    public State sample(State state, Action action) {
        if (action.actionName().equals(ACTION_CHECK)) {
            ObjectParameterizedAction a = (ObjectParameterizedAction) action;
            String n = a.getObjectParameters()[0];
            RockSampleState rsState = (RockSampleState) state;

            // get rover and rock coordinates
            int roverX = (int) rsState.getRoverAtt(ATT_X);
            int roverY = (int) rsState.getRoverAtt(ATT_Y);

            int rockX = (int) rsState.getRockAtt(n, ATT_X);
            int rockY = (int) rsState.getRockAtt(n, ATT_Y);

            setSensorAccuracy(roverX, roverY, rockX, rockY);

            double rand = RandomFactory.getMapped(0).nextDouble();

            String rockQual = (String) rsState.getRockAtt(n, ATT_QUALITY);

            if (this.sensorAccuracy > rand) {
                if (rockQual.equals(ATT_GOOD)) {
                    return this.observationBadRock(n);
                }
                else {
                    return this.observationGoodRock(n);
                }
            }
            else {
                if (rockQual.equals(ATT_GOOD)) {
                    return this.observationGoodRock(n);
                }
                else {
                    return this.observationBadRock(n);
                }
            }

        }
        return state;
    //throw new RuntimeException("Unknown action " + action.actionName() + "; cannot return observation sample.");

    }

    public List<ObservationProbability> probabilities(State state, Action action) {
        return ObservationUtilities.probabilitiesByEnumeration((DiscreteObservationFunction) this, state, action);
    }

    // TODO: probabilities might be a little wonky, but will keep as is for now
    @Override
    public double probability(State observation, State state, Action action){
        String oVal = (String)observation.get(RockSamplePO.ACTION_CHECK);
        String rockVal = (String)state.get(RockSample.CLASS_ROCK);

        if (action.actionName().equals(RockSamplePO.ACTION_CHECK)) {
            return this.sensorAccuracy;
        }
        else {
            return 1.0;
        }
    }
}

