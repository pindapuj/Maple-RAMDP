package taxi.functions.rmaxq;

import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import taxi.Taxi;
import taxi.state.RockSampleState;

public class BaseGetFailurePF extends PropositionalFunction {
	//get fails if any passenger is in the taxi other than desired one  - no abstraction
	
	public BaseGetFailurePF() {
		super("get", new String[]{Taxi.CLASS_PASSENGER});
	}
	
	@Override
	public boolean isTrue(OOState s, String... params) {
		RockSampleState st = (RockSampleState) s;
		
		boolean ret = (boolean) st.getTaxiAtt(Taxi.ATT_TAXI_OCCUPIED);
		return ret;
	}

}
