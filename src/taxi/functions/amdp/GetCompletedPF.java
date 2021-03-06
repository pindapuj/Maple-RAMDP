package taxi.functions.amdp;

import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import taxi.Taxi;
import taxi.hierarchies.tasks.get.TaxiGetDomain;
import utilities.MutableObject;

public class GetCompletedPF extends PropositionalFunction{ 
	// get is complete when desired passenger is in the taxi
	
	public GetCompletedPF() {
		super("get", new String[]{Taxi.CLASS_PASSENGER});
	}
	
	@Override
	public boolean isTrue(OOState s, String... params) {
		String passengerName = params[0];
		MutableObject passenger = (MutableObject) s.object(passengerName);
		String pass_loc = (String) passenger.get(TaxiGetDomain.ATT_LOCATION);
		return pass_loc.equals(TaxiGetDomain.IN_TAXI);
	}
}
