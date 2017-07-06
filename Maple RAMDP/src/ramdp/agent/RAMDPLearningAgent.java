package ramdp.agent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import burlap.behavior.policy.EnumerablePolicy;
import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.planning.Planner;
import burlap.behavior.singleagent.planning.stochastic.rtdp.BoundedRTDP;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.Environment;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.model.TransitionProb;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.HashableStateFactory;
import hierarchy.framework.GroundedTask;
import testing.HierarchicalCharts;
import utilities.ValueIteration;

public class RAMDPLearningAgent implements LearningAgent{
	
	public static final int DEFAULT_MAX_STEPS = 500;

	/**
	 * The root of the task hierarchy
	 */
	private GroundedTask root;
	
	/**
	 * r-max "m" parameter
	 */
	private int rmaxThreshold;
	
	/**
	 * maximum reward
	 */
	private double rmax;
	
	/**
	 * collection of models for each task
	 */
	private Map<GroundedTask, RAMDPModel> models;
	
	/**
	 * Steps currently taken
	 */
	private int steps;
	
	/**
	 * lookup grounded tasks by name task
	 */
	private Map<String, GroundedTask> taskNames;
	
	private double gamma;
	
	private HashableStateFactory hashingFactory;
	
	private double maxDelta;
	/**
	 * 
	 * @param root
	 * @param threshold
	 */
	public RAMDPLearningAgent(GroundedTask root, int threshold, double discount, double rmax,
			HashableStateFactory hs, double delta) {
		this.rmaxThreshold = threshold;
		this.root = root;
		this.gamma = discount;
		this.hashingFactory = hs;
		this.rmax = rmax;
		this.models = new HashMap<GroundedTask, RAMDPModel>();
		this.taskNames = new HashMap<String, GroundedTask>();
		this.maxDelta = delta;
	}
	
	@Override
	public Episode runLearningEpisode(Environment env) {
		System.err.println("Warning, using default MAX_STEPS of: " + RAMDPLearningAgent.DEFAULT_MAX_STEPS);
		return runLearningEpisode(env, RAMDPLearningAgent.DEFAULT_MAX_STEPS);
	}

	@Override
	public Episode runLearningEpisode(Environment env, int maxSteps) {
		steps = 0;
		Episode e = new Episode(env.currentObservation());
		System.out.println("begin RAMDP");
		Episode eOut =  solveTask(root, e, env, maxSteps);
		
		// debug code
		HierarchicalCharts.episodeNumber += 1;
		HierarchicalCharts.episodeNumber = HierarchicalCharts.episodeNumber % 100;
		return eOut;
	}

	protected Episode solveTask(GroundedTask task, Episode e, Environment baseEnv, int maxSteps){
		State baseState = e.stateSequence.get(e.stateSequence.size() - 1);
		State currentState = task.mapState(baseState);
		
		while(!task.isTerminal(currentState) && (steps < maxSteps || maxSteps == -1)){
			Action a = nextAction(task, currentState);
			EnvironmentOutcome result;
			
			if (steps > 490 && (HierarchicalCharts.episodeNumber == 99)) {
				System.out.println(steps);
			}

			GroundedTask action = this.taskNames.get(a.actionName());
			if(action == null){
				addChildrenToMap(task, currentState);
				action = this.taskNames.get(a.actionName());
			}
			if(action.isPrimitive()){
				result = baseEnv.executeAction(a);
				e.transition(result);
				baseState = result.op;
				currentState = result.op;
				steps++;
			}else{
//				System.out.println( " child " + a.actionName());//(task.getGroundedChildTasks(currentState)
				//get child task
				result = task.executeAction(currentState, a);
				e = solveTask(action, e, baseEnv, maxSteps);
				
				baseState = e.stateSequence.get(e.stateSequence.size() - 1);
				currentState = task.mapState(baseState);
				result.op = currentState;
			}
			task.fixReward(result);
			
			//update task model
			RAMDPModel model = getModel(task, currentState);
			model.updateModel(result);
		}
		
		return e;
	}
	
	protected void addChildrenToMap(GroundedTask gt, State s){
		List<GroundedTask> chilkdren = gt.getGroundedChildTasks(s);
		for(GroundedTask child : chilkdren){
			taskNames.put(child.getAction().actionName(), child);
		}
	}
	
	protected Action nextAction(GroundedTask task, State s){
		OOSADomain domain = task.getDomain(getModel(task, s));
		
//		BoundedRTDP plan = new BoundedRTDP(domain, gamma, hashingFactory, new ConstantValueFunction(),
//				new ConstantValueFunction(50), 0.01, -1);
//		plan.setMaxRolloutDepth(100);
//        plan.toggleDebugPrinting(false);
        ValueIteration plan = new ValueIteration(domain, gamma, hashingFactory, maxDelta, 1000);
        plan.setValueFunctionInitialization(new ConstantValueFunction(0.0));
		Policy p = plan.planFromState(s);
		Action a = p.action(s);
//		System.out.println(PolicyUtils.sampleFromActionDistribution((EnumerablePolicy) p,s));
		System.out.println(a.actionName());
		System.out.println(plan.qValue(s, a));
		return a;
	}
	
	protected RAMDPModel getModel(GroundedTask t, State s){
		RAMDPModel model = models.get(t);
				
		if(model == null){
			model = new RAMDPModel(t, this.rmaxThreshold, this.rmax, this.hashingFactory);
			this.models.put(t, model);
		}

//		if(t.toString().startsWith("sol")){
//			System.out.println();
//			System.out.println(s + "\n");
//			List<GroundedTask> children = t.getGroundedChildTasks(s);
//			for(GroundedTask child : children){
//				System.out.println(child);
////				if(child.toString().startsWith("get")){
//					List<TransitionProb> tps = model.transitions(s, child.getAction());
//					for(TransitionProb tp: tps){
//						EnvironmentOutcome eo = tp.eo;
//						System.out.println("\tProbability: " + tp.p);
//						System.out.println("\tState:  " + eo.op);
//						System.out.println("\tReward " + eo.r);
//					}
////				}
//			}
//		}
		return model;
	}
}
