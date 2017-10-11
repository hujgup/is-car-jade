package cos30018.assignment.logic;

import java.io.IOException;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;
import jade.core.AID;
import jade.core.Agent;

@SuppressWarnings("serial")
public class CarAgent extends Agent {
	@Override
	public void setup() {
		Environment data = Environment.createDummyData();
		try {
			// TODO: Instead of passing "new AID()", pass the AID of the scheduling agent
			addBehaviour(new UpdateServerBehaviour(data, CarID.create(getAID(), new AID())));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
