package cos30018.assignment.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;
import cos30018.assignment.data.Timetable;
import cos30018.assignment.utils.handleCarCharge;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class CarAgent extends Agent {
	//private int numOfArguments;
	//private SequentialBehaviour listener;
	private List<Integer> unavailableTimes;
	private List<Integer> finalTimes;
	private handleCarCharge carCharge;
	private List<Integer> chargeTimes = new ArrayList<>();
	
	public void toList(String s, List<Integer> l) {
		String replace = s.replace("[","");
		String replace1 = replace.replace("]","");
		String replace2 = replace1.replace(" ","");
		List<String> arrayList = new ArrayList<String>    (Arrays.asList(replace2.split(",")));
		for(String fav:arrayList){
		    l.add(Integer.parseInt(fav.trim()));
		}
	}
	
	 public void setup() {
		AID master = new AID(getArguments()[0].toString(), false);
		Environment env = Environment.createDummyData();
		CarID thisId = CarID.create(getAID());
		try {
			addBehaviour(new UpdateServerBehaviour(env, thisId, new Function<Boolean, ActionResult<Timetable>>() {
				@Override
				public ActionResult<Timetable> apply(Boolean isConstraintUpdate) {
					ActionResult<Timetable> res;
					try {
						ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
						try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
							ObjectOutputStream oos = new ObjectOutputStream(bos);
							oos.writeBoolean(isConstraintUpdate);
							if (isConstraintUpdate.booleanValue()) {
								oos.writeObject(thisId);
								oos.writeObject(env);
							}
							msg.setByteSequenceContent(bos.toByteArray());
						}
						send(msg);
						ACLMessage response = blockingReceive();
						try (ByteArrayInputStream bis = new ByteArrayInputStream(response.getByteSequenceContent())) {
							ObjectInputStream ois = new ObjectInputStream(bis);
							boolean isError = ois.readBoolean();
							if (isError) {
								res = ActionResult.createError((String)ois.readObject());
							} else {
								res = ActionResult.createResult((Timetable)ois.readObject());
							}
						}
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
						res = ActionResult.createError("ACL error: " + e.getMessage());
					}
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