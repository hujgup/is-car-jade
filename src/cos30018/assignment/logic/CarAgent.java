package cos30018.assignment.logic;

import java.io.IOException;
import cos30018.assignment.data.Environment;
import jade.core.Agent;

@SuppressWarnings("serial")
public class CarAgent extends Agent {
	@Override
	public void setup() {
		Environment data = Environment.createDummyData();
		try {
			addBehaviour(new UpdateServerBehaviour(data, 8080));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
