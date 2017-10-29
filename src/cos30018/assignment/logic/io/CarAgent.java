package cos30018.assignment.logic.io;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;
import cos30018.assignment.logic.ActionResult;
import cos30018.assignment.logic.UpdateServerBehaviour;
import cos30018.assignment.logic.scheduling.InformContent;
import cos30018.assignment.ui.json.TimetableEntryJson;
import cos30018.assignment.utils.Input;
import cos30018.assignment.utils.Mutable;
import cos30018.assignment.utils.Output;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class CarAgent extends Agent {
	private CarID thisId;
	public CarAgent() {
	}
	public CarID getID() {
		return thisId;
	}
	private List<TimetableEntryJson> toEntryList(Map<CarID, List<Integer>> map) {
		LinkedList<TimetableEntryJson> res = new LinkedList<>();
		for (Entry<CarID, List<Integer>> kvp : map.entrySet()) {
			res.add(new TimetableEntryJson(kvp.getKey().getID(), kvp.getValue()));
		}
		return res;
	}
	@Override
	public void setup() {
		Environment env = Environment.createDummyData();
		Object[] args = getArguments();
		thisId = CarID.create((int)args[0], getAID(), (AID)args[1]);
		System.out.println("Port number: " + thisId.getID());
		try {
			addBehaviour(new UpdateServerBehaviour(env, thisId, new Function<Boolean, ActionResult<List<TimetableEntryJson>>>() {
				@SuppressWarnings("unchecked")
				@Override
				public ActionResult<List<TimetableEntryJson>> apply(Boolean isConstraintUpdate) {
					System.out.println(thisId.getID() + " received UI message.");
					Mutable<ActionResult<List<TimetableEntryJson>>> res = new Mutable<>();
					try {
						ACLMessage msg = Output.create(thisId.getScheduler());
						Output.write(msg, oos -> {
							oos.writeObject(InformContent.ACTION_PROPAGATE);
							oos.writeBoolean(isConstraintUpdate);
							if (isConstraintUpdate.booleanValue()) {
								oos.writeObject(env);
							}							
						});
						send(msg);
						System.out.println("Awaiting response...");
						ACLMessage response = blockingReceive();
						System.out.println("Reply received.");
						Input.read(response, ois -> {
							boolean isError = ois.readBoolean();
							if (isError) {
								res.value = ActionResult.createError((String)ois.readObject());
							} else {
								res.value = ActionResult.createResult(toEntryList((Map<CarID, List<Integer>>)ois.readObject()));
							}
						});
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
						res.value = ActionResult.createError("ACL error: " + e.getMessage());
					}
					System.out.println("Got result.");
					return res.value;
				}
			}));
			System.out.println("Car agent started.");
		} catch (IOException e) {
			// Agent physically can't work if this gets thrown, because it means it can't listen for comms from the UI layer
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	 }
}