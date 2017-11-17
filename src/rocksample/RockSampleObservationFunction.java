package rocksample;

import burlap.debugtools.RandomFactory;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.pomdp.observations.ObservationFunction;
import rocksample.state.RockSampleRock;
import rocksample.state.RockSampleState;

//import burlap.mdp.singleagent.GroundedAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static rocksample.RockSamplePO.ACTION_CHECK;

/**
 * Created by steph on 11/9/2017.
 */
public class RockSampleObservationFunction implements ObservationFunction {

    protected double checkAccuracy;

    public RockSampleObservationFunction(){};

    public RockSampleObservationFunction(double checkAccuracy){
        this.checkAccuracy = checkAccuracy;
    }

    //@Override
    public boolean canEnumerateObservation(){
        return false;
    }



    //@Override
    public State sampleObservation(State state, ObjectParameterizedAction action){
        /* Need to include checks for all ACTION_CHECK */
        /* Pass in rock number to the ObservationRock(int rockNum) */

        System.out.println("ENTERED OBS");
        if (action.actionName().equals(ACTION_CHECK)){
            String n = action.getObjectParameters()[0];
            RockSampleState rs_state = (RockSampleState) state;
            int roverX = (int) rs_state.getRoverAtt(RockSample.ATT_X);
            int roverY = (int) rs_state.getRoverAtt(RockSample.ATT_Y);

            int rockX = (int) rs_state.getRockAtt(n, RockSample.ATT_X);
            int rockY = (int) rs_state.getRockAtt(n, RockSample.ATT_Y);

            String rockQual = (String) rs_state.getRockAtt(n, RockSample.ATT_QUALITY);

            /* Now we will do math, though you can move it into observation rock and pass the paramters */
            int dx = (roverX-rockX) * (roverX-rockX); //squaring
            int dy = (roverY-rockY) * (roverY-rockY); //square

            double distance = Math.sqrt(dx+ dy);
            double tunable_constant = 20;

            /* Now, we will apply that awkward function */
            double sensor_efficiency = Math.pow(2,-(distance)/tunable_constant);

            /* get random number, and if less than random number */
            //Random rand = new Random();
            double rand = RandomFactory.getMapped(0).nextDouble();
            if(sensor_efficiency > rand){
                if(rockQual.equals("Good")){
                    rockQual = "Bad";
                }
                else{
                    rockQual = "Good";
                }
            }

            /* have applied the mask */

            /* this is stupid code, b/c I don't want to use a map */
            int indx;
            if(n.equals("Rock0")){
                indx = 0;
            }
            else if(n.equals("Rock1")){
                indx=1;
            }
            else if(n.equals("Rock2")){
                indx = 2;
            }
            else if(n.equals("Rock3")){
                indx=3;
            }
            else{

                System.out.println("ERROR FOOL");
                indx = 0;
            }
            return this.observationRock(indx, rockQual);

        }
        return null;
    }

    //@Override
    public double getObservationProbability(State observation, State state,
                                            ObjectParameterizedAction action){
        return 1.;
    }

    protected State observationRock(int indx, String rockQual){

        /* Go into RockSampleDomain, get the rock value */
        RockSampleState checkRock = new RockSampleState(indx,rockQual);
        //String qual = (String) checkRock.getRockAtt(rock, RockSample.ATT_QUALITY);

        // Now have to do the randomness mask


        return checkRock;
    }


    @Override
    public State sample(State state, Action action){
        // if the action is to check the rock
        ObjectParameterizedAction action_op = (ObjectParameterizedAction) action;
        if (action.actionName().equals(ACTION_CHECK)){
            //   get the value of the rock being checked

            //   get a random number
            //   if r < the accuracy
            //     return the true observation of the rock
            //   else return the opposite

            String n = action_op.getObjectParameters()[0];
            RockSampleState rs_state = (RockSampleState) state;
            int roverX = (int) rs_state.getRoverAtt(RockSample.ATT_X);
            int roverY = (int) rs_state.getRoverAtt(RockSample.ATT_Y);

            int rockX = (int) rs_state.getRockAtt(n, RockSample.ATT_X);
            int rockY = (int) rs_state.getRockAtt(n, RockSample.ATT_Y);

            String rockQual = (String) rs_state.getRockAtt(n, RockSample.ATT_QUALITY);

            /* Now we will do math, though you can move it into observation rock and pass the paramters */
            int dx = (roverX-rockX) * (roverX-rockX); //squaring
            int dy = (roverY-rockY) * (roverY-rockY); //square

            double distance = Math.sqrt(dx+ dy);
            double tunable_constant = 20;

            /* Now, we will apply that awkward function */
            double sensor_efficiency = Math.pow(2,-(distance)/tunable_constant);

            /* get random number, and if less than random number */
            //Random rand = new Random();
            double rand = RandomFactory.getMapped(0).nextDouble();
            if(sensor_efficiency > rand){
                if(rockQual.equals("Good")){
                    rockQual = "Bad";
                }
                else{
                    rockQual = "Good";
                }
            }

            /* have applied the mask */

            /* this is stupid code, b/c I don't want to use a map */
            int indx;
            if(n.equals("Rock0")){
                indx = 0;
            }
            else if(n.equals("Rock1")){
                indx=1;
            }
            else if(n.equals("Rock2")){
                indx = 2;
            }
            else if(n.equals("Rock3")){
                indx=3;
            }
            else{

                System.out.println("ERROR FOOL");
                indx = 0;
            }
            return this.observationRock(indx, rockQual);

        }

        throw new RuntimeException("Unknown action " + action.actionName() + "; cannot return observation sample.");
    }

    @Override
    public double probability(State observation, State state, Action action) {
        String oVal = (String)observation.get(RockSamplePO.ACTION_CHECK);
        String rockVal = (String)state.get(RockSample.CLASS_ROCK);

        ObjectParameterizedAction action_op = (ObjectParameterizedAction) action;
        if (action.actionName().equals(ACTION_CHECK)){
            //   get the value of the rock being checked

            //   get a random number
            //   if r < the accuracy
            //     return the true observation of the rock
            //   else return the opposite

            String n = action_op.getObjectParameters()[0];
            RockSampleState rs_state = (RockSampleState) state;
            int roverX = (int) rs_state.getRoverAtt(RockSample.ATT_X);
            int roverY = (int) rs_state.getRoverAtt(RockSample.ATT_Y);

            int rockX = (int) rs_state.getRockAtt(n, RockSample.ATT_X);
            int rockY = (int) rs_state.getRockAtt(n, RockSample.ATT_Y);

            String rockQual = (String) rs_state.getRockAtt(n, RockSample.ATT_QUALITY);

            /* Now we will do math, though you can move it into observation rock and pass the paramters */
            int dx = (roverX-rockX) * (roverX-rockX); //squaring
            int dy = (roverY-rockY) * (roverY-rockY); //square

            double distance = Math.sqrt(dx+ dy);
            double tunable_constant = 20;

            /* Now, we will apply that awkward function */
            double sensor_efficiency = Math.pow(2,-(distance)/tunable_constant);

            /* get random number, and if less than random number */
            //Random rand = new Random();
            double rand = RandomFactory.getMapped(0).nextDouble();
            if(sensor_efficiency > rand){
                if(rockQual.equals("Good")){
                    rockQual = "Bad";
                }
                else{
                    rockQual = "Good";
                }
            }

            /* have applied the mask */

            /* this is stupid code, b/c I don't want to use a map */
            int indx;
            if(n.equals("Rock0")){
                indx = 0;
            }
            else if(n.equals("Rock1")){
                indx=1;
            }
            else if(n.equals("Rock2")){
                indx = 2;
            }
            else if(n.equals("Rock3")){
                indx=3;
            }
            else{

                System.out.println("ERROR FOOL");
                indx = 0;
            }
            return sensor_efficiency;
        }
        // if action name is check
        // then if the value of the rock is good/bad
        //  return the accuracy
        // otherwise return 0


        throw new RuntimeException("Unknown action " + action.actionName() + "; cannot return observation probability.");
    }

}
