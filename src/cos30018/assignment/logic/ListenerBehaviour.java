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
import cos30018.assignment.data.Car;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;
import cos30018.assignment.data.ImmutableCar;
import cos30018.assignment.utils.LocalTimeRange;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class ListenerBehaviour extends CyclicBehaviour {
	private Environment env;
	private Agent a;
	public ListenerBehaviour(Agent owner) {
		a = owner;
	}
	private void writeTimetable(ObjectOutputStream oos, Map<CarID, List<Integer>> tt) throws IOException {
		oos.writeBoolean(false);
		oos.writeObject(tt);
	}
	private void writeError(ObjectOutputStream oos, String msg) throws IOException {
		oos.writeBoolean(true);
		oos.writeObject(msg);
	}
	@Override
	public void action() {
		ACLMessage msg = a.blockingReceive();
		System.out.println("Got message");
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			ACLMessage response = msg.createReply();
			try {
				response.setPerformative(ACLMessage.INFORM);
				try (ByteArrayInputStream bis = new ByteArrayInputStream(msg.getByteSequenceContent())) {
					ObjectInputStream ois = new ObjectInputStream(bis);
					InformContent ic = (InformContent)ois.readObject();
					if (ic == InformContent.ACTION) {
						boolean isConstraintUpdate = ois.readBoolean();
						if (isConstraintUpdate) {
							Environment env2 = (Environment)ois.readObject();
							// Flooring unavailable times to the hour
							for (ImmutableCar car : env2.getAllCars().values()) {
								for (LocalTimeRange range : car.getUnavailableTimes()) {
									range.getLowerBound().getPivot().minusMinutes(range.getLowerBound().getPivot().getMinute());
									range.getLowerBound().setInclusive(true);
									range.getUpperBound().getPivot().minusMinutes(range.getUpperBound().getPivot().getMinute());
									range.getUpperBound().setInclusive(true);
								}
							}
							// Merging environments
							if (env == null) {
								env = env2;
							} else {
								System.out.println("Init");
								env.getAllCars().keySet().stream().forEach(x -> System.out.println(x.getID()));
								System.out.println("Spec'd");
								env2.getAllCars().keySet().stream().forEach(x -> System.out.println(x.getID()));
								LinkedList<Entry<CarID, Car>> toReplace = new LinkedList<>();
								for (Entry<CarID, Car> kvp : env2.getAllCars().entrySet()) {
									if (env.hasCar(kvp.getKey())) {
										System.out.println("Has " + kvp.getKey().getID());
										toReplace.add(kvp);
									} else {
										System.out.println("New " + kvp.getKey().getID());
										env.addCar(kvp.getValue());
									}
								}
								System.out.println("To replace");
								toReplace.stream().forEach(x -> System.out.println(x.getKey().getID()));
								for (Entry<CarID, Car> kvp : toReplace) {
									env.removeCar(kvp.getKey());
									env.addCar(kvp.getValue());
								}
								System.out.println("End");
								env.getAllCars().keySet().stream().forEach(x -> System.out.println(x.getID()));
							}
						}
						if (env != null) {
							writeTimetable(oos, new ConstraintSolver(env).get());
						} else {
							writeError(oos, "Cannot negotiate a timetable when the environment has not been set.");								
						}
					} else if (ic == InformContent.REMOVE) {
						CarID cid = CarID.fromID(ois.readInt());
						if (env != null && env.hasCar(cid)) {
							env.removeCar(cid);
						}
						oos.writeBoolean(false);
					} else {
						writeError(oos, "Unknown inform content " + ic.toString());
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				writeError(oos, e.getMessage());
			}
			System.out.println("Sending reply");
			oos.flush();
			bos.flush();
			response.setByteSequenceContent(bos.toByteArray());
			a.send(response);
		} catch (IOException e) {
			e.printStackTrace();					
		}
	}
}
