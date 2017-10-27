package cos30018.assignment.logic;

import jade.core.Agent;

@SuppressWarnings("serial")
public class SchedulingAgent extends Agent {
	@Override
	protected void setup() {
		addBehaviour(new ListenerBehaviour(this));	
	}
		
}

