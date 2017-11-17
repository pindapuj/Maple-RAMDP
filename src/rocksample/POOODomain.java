package rocksample;

import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.mdp.singleagent.pomdp.PODomain;
import burlap.mdp.singleagent.pomdp.observations.ObservationFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steph on 11/9/2017.
 * Basically reimplement OOSADomain but using things that
 * are partially observable -- taking place of OOSA Domain
 */
public class POOODomain extends PODomain implements OODomain
{    protected Map<String, Class<?>> stateClassesMap = new HashMap();
    protected List<ActionType> actionTypes = new ArrayList();
    protected Map<String, ActionType> actionMap = new HashMap();


    public POOODomain(){};
    @Override
    public List<Class<?>> stateClasses() {
        return null;
    }

    @Override
    public Class<?> stateClass(String s) {
        return null;
    }

    @Override
    public POOODomain addStateClass(String s, Class<?> aClass) {
        this.stateClassesMap.put(s, aClass);
        return this;
    }

    @Override
    public List<PropositionalFunction> propFunctions() {
        return null;
    }

    @Override
    public PropositionalFunction propFunction(String s) {
        return null;
    }

    @Override
    public POOODomain addPropFunction(PropositionalFunction propositionalFunction) {
        return null;
    }


    public ObservationFunction getObservationFunction(){
        return this.obsevationFunction ;
    }

    @Override
    public void setObservationFunction(ObservationFunction observationFunction){
       this.obsevationFunction = observationFunction;
    }

    /* May need to remove
    public List<ActionType> getActionTypes() {
        return new ArrayList(this.actionTypes);
    }

    public ActionType getAction(String name) {
        return (ActionType)this.actionMap.get(name);
    } */

}
