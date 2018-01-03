package rocksample;

import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.oo.ObjectParameterizedActionType;

/**
 * Created by steph on 10/28/2017.
 */
public class CheckActionType extends ObjectParameterizedActionType{

    public CheckActionType(String name, String[] parameterClasses){
        super(name, parameterClasses);
    }

    @Override
    protected boolean applicableInState(State s, ObjectParameterizedAction objAction) {
        return true;
    }

}