package rocksample;

import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import rocksample.state.RockSampleRock;
import rocksample.state.RockSampleWall;
import rocksample.state.RoverAgent;
import rocksample.POOODomain;
import rocksample.stateGenerator.RockSampleStateFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steph on 11/9/2017.
 */
public class RockSamplePO implements DomainGenerator {

    // object classes
    public static final String CLASS_ROVER = 				"Rover";
    public static final String CLASS_ROCK =                 "Rock";
    public static final String CLASS_WALL =                 "Wall";

    // abstract class
    public static final String CLASS_PT =                   "Point";

    // common attributes
    public static final String ATT_X =                      "x";
    public static final String ATT_Y =                      "y";

    // rock attributes
    public static final String ATT_QUALITY =                "quality";

    // wall attributes
    public static final String ATT_START_X = 				"startX";
    public static final String ATT_START_Y = 				"startY";
    public static final String ATT_LENGTH = 				"length";
    public static final String ATT_IS_HORIZONTAL =			"isHorizontal";

    // colors
    public static final String COLOR_RED = 					"red";
    public static final String COLOR_YELLOW = 				"yellow";
    public static final String COLOR_GREEN = 				"green";
    public static final String COLOR_BLUE = 				"blue";
    public static final String COLOR_MAGENTA =				"magenta";
    public static final String COLOR_BLACK = 				"black";
    public static final String COLOR_GRAY =					"gray";

    // actions
    public static final int NUM_MOVE_ACTIONS = 				4;
    public static final String ACTION_NORTH = 				"north";
    public static final String ACTION_EAST =				"east";
    public static final String ACTION_SOUTH =				"south";
    public static final String ACTION_WEST = 				"west";
    public static final String ACTION_SAMPLE =              "sample";
    public static final String ACTION_CHECK =               "check";

    // action indexes
    public static int IND_NORTH = 							0;
    public static int IND_EAST = 							1;
    public static int IND_SOUTH = 							2;
    public static int IND_WEST = 							3;
    public static int IND_SAMPLE =                          4;
    public static int IND_CHECK =                           5;

    // parameters dictating probabilities of the model
    private RewardFunction rf;
    private TerminalFunction tf;
    private double[][] moveDynamics;
    private boolean noisy;             // whether the sensor is noisy or not
    private double noisyProbability;   // prob sensor is accurate


    public RockSamplePO(RewardFunction r, TerminalFunction t,
                      double correctMoveProb, boolean n, double noisyProb) {
        rf = r;
        tf = t;
        this.noisy = n;
        this.noisyProbability = noisyProb;
        setMoveDynamics(correctMoveProb);
    }

    public RockSamplePO(double correctMoveProb, boolean n, double noisyProb) {
        this.noisy = n;
        this.noisyProbability = noisyProb;
        setMoveDynamics(correctMoveProb);
        this.rf = new RockSampleRewardFunction();
        this.tf = new RockSampleTerminalFunction();
    }

    public RockSamplePO(double[][] movement, boolean n, double noisyProb) {
        this.noisy = n;
        this.noisyProbability = noisyProb;
        this.moveDynamics = movement;
        this.rf = new RockSampleRewardFunction();
        this.tf = new RockSampleTerminalFunction();
    }

    public RockSamplePO() { this(1, false, 0); }

    private void setMoveDynamics(double correctProb) {
        moveDynamics = new double[NUM_MOVE_ACTIONS][NUM_MOVE_ACTIONS];

        for(int choose = 0; choose < NUM_MOVE_ACTIONS; choose++){
            for(int outcome = 0; outcome < NUM_MOVE_ACTIONS; outcome++){
                if(choose == outcome){
                    moveDynamics[choose][outcome] = correctProb;
                }
                // the two directions which are one away get the rest of prob
                else if(Math.abs(choose - outcome) % 2 == 1){
                    moveDynamics[choose][outcome] = (1 - correctProb) / 2;
                }else{
                    moveDynamics[choose][outcome] = 0;
                }
            }
        }
    }

    public POOODomain generateDomain(){
        POOODomain domain = new POOODomain();
        // add the state classes to the domain
        domain.addStateClass(CLASS_ROVER, RoverAgent.class)
                .addStateClass(CLASS_ROCK, RockSampleRock.class)
                .addStateClass(CLASS_WALL, RockSampleWall.class);

        // make a new rocksample model
        RockSampleModel model = new RockSampleModel(moveDynamics);
        FactoredModel rockSampleModel = new FactoredModel(model, rf, tf);
        domain.setModel(rockSampleModel);

        domain.addActionTypes(
                new UniversalActionType(ACTION_NORTH),
                new UniversalActionType(ACTION_SOUTH),
                new UniversalActionType(ACTION_EAST),
                new UniversalActionType(ACTION_WEST),
                new UniversalActionType(ACTION_SAMPLE),

                // check is object parameterized action
                new CheckActionType(ACTION_CHECK, new String[]{CLASS_ROCK}));

        return domain;
    }

    public static void main(String[] args) {
        RockSamplePO rocksampleBuild = new RockSamplePO();
        POOODomain domain = rocksampleBuild.generateDomain();

        HashableStateFactory hs = new SimpleHashableStateFactory();

        State s = RockSampleStateFactory.createClassicState();
        SimulatedEnvironment env = new SimulatedEnvironment(domain, s);

        List<Episode> eps = new ArrayList<Episode>();
        QLearning qagent = new QLearning(domain, 0.95, hs, 0, 0.01);

        for(int i = 0; i < 1000; i++){
            Episode e = qagent.runLearningEpisode(env, 5000);
            System.out.println(e.rewardSequence);
            eps.add(e);
            env.resetEnvironment();
        }

        EpisodeSequenceVisualizer v = new EpisodeSequenceVisualizer(RockSampleVisualizer.getVisualizer(5, 5),
                domain, eps);
        v.setDefaultCloseOperation(v.EXIT_ON_CLOSE);
        v.initGUI();
    }

}
