package rocksample.POOO;

import burlap.behavior.singleagent.auxiliary.StateEnumerator;
import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.singleagent.pomdp.PODomain;
import burlap.mdp.singleagent.pomdp.observations.ObservationFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steph on 11/9/2017.
 * Good - works (checked with Tiger)
 */

public class POOODomain extends PODomain implements OODomain
{
    protected Map<String, Class<?>> stateClassesMap = new HashMap<String, Class<?>>();

    protected Map<String, PropositionalFunction> propFunctionMap = new HashMap<String, PropositionalFunction>();

    /**
     *      getters & setters
     */

    // stateClasses
    // Returns the list of state classes
    @Override
    public List<Class<?>> stateClasses() {
        return new ArrayList<Class<?>>(stateClassesMap.values());
    }

    // stateClass
    // Given the name of a class, returns the class
    @Override
    public Class<?> stateClass(String className) {
        return stateClassesMap.get(className);
    }

    // addStateClass
    // Given the name of a class and a class, adds the state class to the
    // domain and returns
    @Override
    public POOODomain addStateClass(String className, Class<?> stateClass) {
        this.stateClassesMap.put(className, stateClass);
        return this;
    }

    // propFunctions
    // Returns the list of propositional functions
    @Override
    public List<PropositionalFunction> propFunctions() {
        return new ArrayList<PropositionalFunction>(
                this.propFunctionMap.values());
    }

    // propFunction
    // Given the name of a propositional function, returns the
    // propositional function
    @Override
    public PropositionalFunction propFunction(String name) {
        return this.propFunctionMap.get(name);
    }

    // addPropFunction
    // Given a propositional function, adds the propositional function
    // to the domain and returns
    @Override
    public POOODomain addPropFunction(PropositionalFunction prop) {
        this.propFunctionMap.put(prop.getName(), prop);
        return this;
    }

    // getObserationFunction
    // Returns the observation function
    public ObservationFunction getObserationFunction() {
        return this.obsevationFunction;
    }

    // setObservationFunction
    // Given an observation function, sets the observation function
    @Override
    public void setObservationFunction(ObservationFunction obsFn) {
       this.obsevationFunction = obsFn;
    }

    // providesStateEnumerator
    // Returns true if provides a state enumerator, false otherwise
    public boolean providesStateEnumerator() {
        return this.stateEnumerator != null;
    }

    // getStateEnumerator
    // Return state enumerator if it exists; otherwise, throw exception
    @Override
    public StateEnumerator getStateEnumerator() {
        if(this.stateEnumerator == null){
            throw new RuntimeException("This domain cannot return a StateEnumerator because one is not defined for it. "
                    + "Use the providesStateEnumerator() method to check if one is provided in advance.");
        }
        return stateEnumerator;
    }

    // setStateEnumerator
    // Given a state enumerator, sets the state enumerator
    @Override
    public void setStateEnumerator(StateEnumerator stateEnumerator) {
        this.stateEnumerator = stateEnumerator;
    }
}
