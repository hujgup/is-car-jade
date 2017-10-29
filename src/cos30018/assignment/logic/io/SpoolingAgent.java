package cos30018.assignment.logic.io;

import java.io.IOException;
import jade.core.Agent;
import jade.wrapper.AgentContainer;

@SuppressWarnings("serial")
public class SpoolingAgent extends Agent {
	@Override
	public void setup() {
		try {
			addBehaviour(new SpoolingServerBehaviour(this, (AgentContainer)getArguments()[0]));
			System.out.println("Spooling agent started.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
