package rocksample;

import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.auxiliary.StateEnumerator;
import burlap.behavior.singleagent.pomdp.BeliefPolicyAgent;
import burlap.behavior.singleagent.pomdp.qmdp.QMDP;
import burlap.behavior.singleagent.pomdp.wrappedmdpalgs.BeliefSparseSampling;
import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.pomdp.PODomain;
import burlap.mdp.singleagent.pomdp.beliefstate.BeliefState;
import burlap.mdp.singleagent.pomdp.beliefstate.TabularBeliefState;
import burlap.mdp.singleagent.pomdp.observations.ObservationFunction;
import burlap.shell.visual.VisualExplorer;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.ReflectiveHashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import burlap.visualizer.Visualizer;
import rocksample.POOO.POOOBeliefAgent;
import rocksample.state.RockSampleRock;
import rocksample.state.RockSampleWall;
import rocksample.state.RoverAgent;
import rocksample.POOO.POOODomain;
import rocksample.stateGenerator.RockSampleStateFactory;
import rocksample.POOO.SimulatedPOOOEnvironment;

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
    public static final String ATT_GOOD =                   "Good";
    public static final String ATT_BAD =                    "Bad";
    public static final int NUM_ROCKS =                     4;

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

    // rewards
    private double exitAreaReward =                         10.;
    private double goodRockReward =                         10.;
    private double badRockReward =                          -10.;
    private double noReward =                               0;

    // parameters dictating probabilities of the model
    private RewardFunction rf;
    private TerminalFunction tf;
    private boolean noisy;             // whether the sensor is noisy or not
    private double noisyProbability;   // prob sensor is accurate


    public String[] observations = new String[NUM_ROCKS];

    public RockSamplePO(RewardFunction r, TerminalFunction t, boolean n, double noisyProb) {
        rf = r;
        tf = t;
        this.noisy = n;
        this.noisyProbability = noisyProb;
    }

    public RockSamplePO(boolean n, double noisyProb) {
        this.noisy = n;
        this.noisyProbability = noisyProb;
        this.rf = new RockSampleRewardFunction();
        this.tf = new RockSampleTerminalFunction();
    }

    public RockSamplePO() { this(false, 0); }

    public POOODomain generateDomain() {
        POOODomain domain = new POOODomain();
        // add the state classes to the domain
        domain.addStateClass(CLASS_ROVER, RoverAgent.class)
                .addStateClass(CLASS_ROCK, RockSampleRock.class)
                .addStateClass(CLASS_WALL, RockSampleWall.class);

        domain.addActionTypes(
                new UniversalActionType(ACTION_NORTH),
                new UniversalActionType(ACTION_SOUTH),
                new UniversalActionType(ACTION_EAST),
                new UniversalActionType(ACTION_WEST),
                new UniversalActionType(ACTION_SAMPLE),

                // check is object parameterized action
                new CheckActionType(ACTION_CHECK, new String[]{CLASS_ROCK}));

        // add observation function
        ObservationFunction of = new RockSampleObservationFunction(this.noisyProbability);
        domain.setObservationFunction(of);

        StateEnumerator senum = new StateEnumerator(domain, new SimpleHashableStateFactory());

        // make a new rocksample model
        RockSamplePOModel model = new RockSamplePOModel(exitAreaReward, goodRockReward, badRockReward, noReward);
        domain.setModel(model);

        RockSampleStateFactory rs_statefactory = new RockSampleStateFactory();

        // TODO: seems excessive. can we make this simpler to add in new rocks?
        senum.getEnumeratedID(rs_statefactory.createCustomState("Good", "Good", "Good", "Good"));
        senum.getEnumeratedID(rs_statefactory.createCustomState("Bad", "Good", "Good", "Good"));
        senum.getEnumeratedID(rs_statefactory.createCustomState("Good", "Bad", "Good", "Good"));
        senum.getEnumeratedID(rs_statefactory.createCustomState("Good", "Good", "Bad", "Good"));
        senum.getEnumeratedID(rs_statefactory.createCustomState("Good", "Good", "Good", "Bad"));

        senum.getEnumeratedID(rs_statefactory.createCustomState("Bad", "Bad", "Good", "Good"));
        senum.getEnumeratedID(rs_statefactory.createCustomState("Bad", "Good", "Bad", "Good"));
        senum.getEnumeratedID(rs_statefactory.createCustomState("Bad", "Good", "Good", "Bad"));
        senum.getEnumeratedID(rs_statefactory.createCustomState("Good", "Bad", "Bad", "Good"));

        senum.getEnumeratedID(rs_statefactory.createCustomState("Good", "Bad", "Good", "Bad"));
        senum.getEnumeratedID(rs_statefactory.createCustomState("Good", "Good", "Bad", "Bad"));
        senum.getEnumeratedID(rs_statefactory.createCustomState("Bad", "Bad", "Bad", "Good"));
        senum.getEnumeratedID(rs_statefactory.createCustomState("Bad", "Good", "Bad", "Bad"));

        senum.getEnumeratedID(rs_statefactory.createCustomState("Bad", "Bad", "Good", "Bad"));
        senum.getEnumeratedID(rs_statefactory.createCustomState("Good", "Bad", "Bad", "Bad"));
        senum.getEnumeratedID(rs_statefactory.createCustomState("Bad", "Bad", "Bad", "Bad"));

        domain.setStateEnumerator(senum);

        return domain;
    }

    // just returns a random ass state casted to a belief state but ok
    public static BeliefState getInitialBeliefState(POOODomain domain) {
        TabularBeliefState bs = new TabularBeliefState(domain, domain.getStateEnumerator());
        bs.initializeBeliefsUniformly();
        return bs;
    }

    public static QMDP getQMDPagent(PODomain domain, HashableStateFactory hs,
                                    RockSampleRewardFunction rf,
                                    RockSampleTerminalFunction tf){
        QMDP qagent = new QMDP(domain, rf, tf, 0.99, hs, 0.001, 1000);
        return qagent;

    }

    public static void main(String[] args) {
        RockSamplePO rsBuild = new RockSamplePO();
        POOODomain domain = rsBuild.generateDomain();
        BeliefState initialBelief = RockSamplePO.getInitialBeliefState(domain);
        HashableStateFactory rs = new ReflectiveHashableStateFactory();
        // TODO: change hardcoded values
        BeliefSparseSampling bss = new BeliefSparseSampling(domain, 0.99, rs, 10, -1);
        Policy p = new GreedyQPolicy(bss);
        State s = RockSampleStateFactory.createClassicState();

        SimulatedEnvironment poooEnv = new SimulatedPOOOEnvironment(domain, s);
        poooEnv.setCurStateTo(s);
        SimulatedEnvironment envToUse = poooEnv;

        BeliefPolicyAgent agent = new BeliefPolicyAgent(domain, envToUse, p);
        //POOOBeliefAgent agent = new POOOBeliefAgent(domain, envToUse, p);

        agent.setBeliefState(initialBelief);
        agent.setEnvironment(poooEnv);

        Episode ea = agent.actUntilTerminalOrMaxSteps(100);
        for(int i = 0; i < ea.numTimeSteps()-1; i++){
            System.out.println(ea.action(i) + " " + ea.reward(i+1));
        }

       /* EpisodeSequenceVisualizer vis = new EpisodeSequenceVisualizer(RockSampleVisualizer.getVisualizer(5, 5),
                domain, outputPath);
        vis.setDefaultCloseOperation(vis.EXIT_ON_CLOSE);
        vis.initGUI(); */

      //Visualizer v = RockSampleVisualizer.getVisualizer(5,5);

       //VisualExplorer exp = new VisualExplorer(domain, v, s);
      //  VisualExplorer exp = new VisualExplorer(domain, envToUse, v);
      /*  exp.addKeyAction("w",ACTION_NORTH,"");
        exp.addKeyAction("s",ACTION_SOUTH,"");
        exp.addKeyAction("d",ACTION_EAST,"");
        exp.addKeyAction("a",ACTION_WEST,"");
        exp.addKeyAction("x",ACTION_CHECK,"");
        exp.addKeyAction("q",ACTION_SAMPLE,"");*/

       // exp.initGUI();

      //  EnvironmentShell shell = new EnvironmentShell(domain, envToUse);
       // shell.start();

    }

}

