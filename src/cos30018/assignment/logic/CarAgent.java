package cos30018.assignment.logic;

import java.io.IOException;
import cos30018.assignment.data.SystemData;
import jade.core.Agent;

@SuppressWarnings("serial")
public class CarAgent extends Agent {
	@Override
	public void setup() {
		SystemData data = SystemData.createDummyData();
		try {
			addBehaviour(new UpdateServerBehaviour(data, 8080));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
