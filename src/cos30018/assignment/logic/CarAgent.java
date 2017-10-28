package cos30018.assignment.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;
import cos30018.assignment.ui.json.TimetableEntryJson;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class CarAgent extends Agent {
	private List<TimetableEntryJson> toEntryList(Map<CarID, List<Integer>> map) {
		LinkedList<TimetableEntryJson> res = new LinkedList<>();
		for (Entry<CarID, List<Integer>> kvp : map.entrySet()) {
			res.add(new TimetableEntryJson(kvp.getKey().getID(), kvp.getValue()));
		}
		return res;
	}
	@Override
	public void setup() {
		AID master = new AID("master", false);
		Environment env = Environment.createDummyData();
		CarID thisId = CarID.create(getAID());
		System.out.println("Port number: " + thisId.getID());
		try {
			addBehaviour(new UpdateServerBehaviour(env, thisId, new Function<Boolean, ActionResult<List<TimetableEntryJson>>>() {
				@SuppressWarnings("unchecked")
				@Override
				public ActionResult<List<TimetableEntryJson>> apply(Boolean isConstraintUpdate) {
					System.out.println(thisId.getID() + " received UI message.");
					ActionResult<List<TimetableEntryJson>> res;
					try {
						ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
						msg.addReceiver(master);
						try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
							ObjectOutputStream oos = new ObjectOutputStream(bos);
							oos.writeBoolean(isConstraintUpdate);
							if (isConstraintUpdate.booleanValue()) {
								oos.writeObject(env);
							}
							bos.flush();
							msg.setByteSequenceContent(bos.toByteArray());
						}
						send(msg);
						System.out.println("Awaiting response...");
						ACLMessage response = blockingReceive();
						try (ByteArrayInputStream bis = new ByteArrayInputStream(response.getByteSequenceContent())) {
							ObjectInputStream ois = new ObjectInputStream(bis);
							boolean isError = ois.readBoolean();
							if (isError) {
								res = ActionResult.createError((String)ois.readObject());
							} else {
								res = ActionResult.createResult(toEntryList((Map<CarID, List<Integer>>)ois.readObject()));
							}
						}
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
						res = ActionResult.createError("ACL error: " + e.getMessage());
					}
					System.out.println("Got result.");
					return res;
				}
			}));
		} catch (IOException e) {
			// Agent physically can't work if this gets thrown, because it means it can't listen for comms from the UI layer
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	 }
}