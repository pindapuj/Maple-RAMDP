package rocksample.POOO;

import burlap.mdp.auxiliary.StateGenerator;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.NullState;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.environment.extensions.EnvironmentObserver;
import burlap.mdp.singleagent.pomdp.SimulatedPOEnvironment;
import rocksample.POOO.POOODomain;

/**
 * Created by steph on 11/24/2017.
 * TODO: make sure there aren't more aspects of an OO environment that need to
 *       be included
 *       why are you bad
 */

public class SimulatedPOOOEnvironment extends SimulatedPOEnvironment {

    protected State currObservation = NullState.instance;

    protected POOODomain poooDomain;

    /**
     *      Constructors
     */

    // SimulatedPOOOEnvrionment
    // Given a domain, creates the simulated POOOEnvironment
    public SimulatedPOOOEnvironment(POOODomain domain){
        super(domain);
        this.poooDomain = domain;
    }

    // SimulatedPOOOEnvrionment
    // Given a domain and an initial hidden state, creates the
    // simulated POOOEnvironment
    public SimulatedPOOOEnvironment(POOODomain domain,
                                    State initialHiddenState){
        super(domain, initialHiddenState);
        if(domain.getModel() == null)
        {
            throw new RuntimeException("u dont have a model");
        }
        if(domain == null){
            throw new RuntimeException("no domain");
        }
        this.poooDomain = domain;
    }

    // SimulatedPOOOEnvironment
    // Given a domain and a hidden state generator, creates the simulated
    //   POOOEnvironment
    public SimulatedPOOOEnvironment(POOODomain domain,
                                    StateGenerator hiddenStateGenerator){
        super(domain, hiddenStateGenerator);
        this.poooDomain = domain;
    }

    // setCurrObservation
    // Given an observation, sets the current observation of the agent to
    // the observation
    public void setCurrObservationTo(State observation){
        this.currObservation = observation;
    }

    // currentObservation
    // Returns the last observation made by the agent
    @Override
    public State currentObservation(){
        return this.currObservation;
    }

    // getCurrentHiddenState
    // Returns the current hidden state
    public State getCurrentHiddenState(){
        return this.curState;
    }

    // executeAction
    // Given an action, returns the outcome in the environment that occurs
    // when the action is taken
    @Override
    public EnvironmentOutcome executeAction(Action a){
        for(EnvironmentObserver observer : this.observers){
            observer.observeEnvironmentActionInitiation(
                    this.currentObservation(), a);
        }

        State nextObservation = curObservation;

        EnvironmentOutcome eo;

        // if the action is allowed or the agent is not in a terminal state
        if(this.allowActionFromTerminalStates || !this.isInTerminalState()){
            eo = model.sample(this.curState, a);
            nextObservation = poooDomain.getObservationFunction().sample(eo.op,
                                                                            a);
        }
        else{
            eo = new EnvironmentOutcome(this.curState, a,
                                        this.curState.copy(),
                                        0., true);
        }

        // update the reward, term, and current state
        this.lastReward = eo.r;
        this.terminated = eo.terminated;
        this.curState = eo.op;

        EnvironmentOutcome observedOutcome = new EnvironmentOutcome(
                this.curObservation, a,
                nextObservation, eo.r,
                this.terminated);

        this.curObservation = nextObservation;

        for(EnvironmentObserver observer : this.observers()){
            observer.observeEnvironmentInteraction(observedOutcome);
        }

        return observedOutcome;
    }

    // resetEnvironment
    // Resets the environment
    @Override
    public void resetEnvironment(){
        super.resetEnvironment();
        this.curObservation = NullState.instance;
    }
}
