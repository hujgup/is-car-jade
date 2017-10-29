package cos30018.assignment.logic.scheduling;

import jade.core.Agent;

@SuppressWarnings("serial")
public class SchedulingAgent extends Agent {
	@Override
	protected void setup() {
		int selfId = (int)getArguments()[0];
		addBehaviour(new ListenerBehaviour(this, selfId));	
		System.out.println("Scheduling agent started.");
	}
}

