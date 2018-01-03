package rocksample.POOO;

import burlap.behavior.singleagent.Episode;
import burlap.mdp.core.action.Action;
import burlap.mdp.singleagent.environment.Environment;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.pomdp.beliefstate.BeliefState;
import burlap.mdp.singleagent.pomdp.beliefstate.BeliefUpdate;
import burlap.mdp.singleagent.pomdp.beliefstate.TabularBeliefUpdate;

// note: not sure if this implementation is correct...

public class POOOBeliefAgent{

    public abstract class BeliefAgent{
        protected Environment environment;
        protected POOOBeliefState curBelief;
        protected POOODomain poooDomain;
        protected BeliefUpdate updater;

        public BeliefAgent(POOODomain pd, Environment environment) {
            this.poooDomain = pd;
            this.environment = environment;
            this.updater = new TabularBeliefUpdate(pd);
        }

        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }

        public void setBeliefState(POOOBeliefState beliefState) {
            this.curBelief = beliefState;
        }

        public BeliefUpdate getUpdater() {
            return this.updater;
        }

        public void setUpdater(BeliefUpdate updater) {
            this.updater = updater;
        }

        public Episode actUntilTerminal() {
            Episode ea = new Episode();
            ea.initializeInState(this.environment.currentObservation());

            while(!this.environment.isInTerminalState()) {
                Action ga = this.getAction(this.curBelief);
                EnvironmentOutcome eo = this.environment.executeAction(ga);
                ea.transition(ga, eo.op, eo.r);
                BeliefState bf = this.updater.update(this.curBelief, eo.op, eo.a);
                POOOBeliefState pbf = (POOOBeliefState) bf;
                this.curBelief = pbf;
            }

            return ea;
        }

        public Episode actUntilTerminalOrMaxSteps(int maxSteps) {
            Episode ea = new Episode();
            ea.initializeInState(this.environment.currentObservation());

            for(int c = 0; !this.environment.isInTerminalState() && c < maxSteps; ++c) {
                Action ga = this.getAction(this.curBelief);
                EnvironmentOutcome eo = this.environment.executeAction(ga);
                ea.transition(ga, eo.op, eo.r);
                BeliefState bf = this.updater.update(this.curBelief, eo.op, eo.a);
                POOOBeliefState pbf = (POOOBeliefState) bf;
                this.curBelief = pbf;
            }

            return ea;
        }

        public abstract Action getAction(POOOBeliefState var1);
    }
}
