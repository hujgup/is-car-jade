package cos30018.assignment.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import cos30018.assignment.data.CarID;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class Output {
	public static interface OosConsumer {
		void accept(ObjectOutputStream oos) throws IOException;
	}
	
	private Output() {
	}
	public static ACLMessage create(Agent self, Iterable<CarID> sendTo) {
		ACLMessage res = new ACLMessage(ACLMessage.INFORM);
		if (sendTo != null) {
			for (CarID cid : sendTo) {
				if (!self.getAID().equals(cid.getScheduler())) {
					res.addReceiver(cid.getScheduler());
				}
			}
		}
		return res;
	}
	public static ACLMessage create(Iterable<AID> sendTo) {
		ACLMessage res = new ACLMessage(ACLMessage.INFORM);
		for (AID aid : sendTo) {
			res.addReceiver(aid);
		}
		return res;
	}
	public static ACLMessage create(AID sendTo) {
		ACLMessage res = new ACLMessage(ACLMessage.INFORM);
		res.addReceiver(sendTo);
		return res;
	}
	public static ACLMessage create() {
		return create(null, null);
	}
	public static void write(ACLMessage msg, OosConsumer writer) throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			writer.accept(oos);
			oos.flush();
			bos.flush();
			msg.setByteSequenceContent(bos.toByteArray());
		}
	}
	public static void awaitResponses(Agent a, int amount) {
		for (int i = 0; i < amount; i++) {
			a.blockingReceive();
		}
	}
}
