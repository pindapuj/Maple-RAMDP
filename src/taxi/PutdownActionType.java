package taxi;

/*import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.oo.ObjectParameterizedActionType;
import taxi.hierarchies.tasks.dropoff.TaxiDropoffDomain;
import taxi.hierarchies.tasks.dropoff.state.TaxiDropoffState;
import taxi.state.RockSampleState;

public class PutdownActionType extends ObjectParameterizedActionType {
    public PutdownActionType(String name, String[] parameterClasses) {
        super(name, parameterClasses);
    }

    @Override
    protected boolean applicableInState(State s, ObjectParameterizedAction objectParameterizedAction) {
        RockSampleState state = (RockSampleState) s;
        String[] params = objectParameterizedAction.getObjectParameters();
        String passengerName = params[0];
        ObjectInstance passenger = state.object(passengerName);

        // Must be at a depot
        int px = (int)state.getPassengerAtt(passengerName, Taxi.ATT_X);
        int py = (int)state.getPassengerAtt(passengerName, Taxi.ATT_Y);
        for(String loc : state.getLocations()) {
            int lx = (int)state.getLocationAtt(loc, Taxi.ATT_X);
            int ly = (int)state.getLocationAtt(loc, Taxi.ATT_Y);

            if(lx == px && ly == py) {
                return true;
            }
        }

        return true;
    }
}*/

