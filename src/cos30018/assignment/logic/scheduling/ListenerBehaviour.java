package cos30018.assignment.logic.scheduling;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import cos30018.assignment.data.Car;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;
import cos30018.assignment.data.ImmutableCar;
import cos30018.assignment.utils.Input;
import cos30018.assignment.utils.LocalTimeRange;
import cos30018.assignment.utils.Output;
import cos30018.assignment.utils.RuntimeClassNotFoundException;
import cos30018.assignment.utils.RuntimeFIPAException;
import cos30018.assignment.utils.RuntimeIOException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class ListenerBehaviour extends CyclicBehaviour {
	private Environment env;
	private Agent a;
	private int id;
	private Consumer<ObjectInputStream> returnListener;
	public ListenerBehaviour(Agent owner, int id) {
		a = owner;
		this.id = id;
	}
	private void writeTimetable(ObjectOutputStream oos, Map<CarID, List<Integer>> tt) throws IOException {
		oos.writeBoolean(false);
		oos.writeObject(tt);
	}
	private void writeError(ObjectOutputStream oos, String msg) throws IOException {
		oos.writeBoolean(true);
		oos.writeObject(msg);
	}
	public void setReturnListener(Consumer<ObjectInputStream> callback) {
		returnListener = callback;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void action() {
		ACLMessage msg = a.blockingReceive();
		System.out.println("Got message");
		final ACLMessage response = msg.createReply();
		boolean ran = true;
		try {
			Output.write(response, oos -> {
				try {
					response.setPerformative(ACLMessage.INFORM);
					Input.read(msg, ois -> {
						InformContent ic = (InformContent)ois.readObject();
						if (ic == InformContent.ACTION_PROPAGATE || ic == InformContent.ACTION) {
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
							if (ic == InformContent.ACTION_PROPAGATE) {
								ACLMessage msg2 = Output.create(a, env.getAllCars().keySet());
								Output.write(msg2, oos2 -> {
									oos2.writeObject(InformContent.ACTION);
									oos2.writeObject(env);
								});
								a.send(msg2);
								Output.awaitResponses(a, env.getAllCars().size() - 1);
								if (env != null) {
									writeTimetable(oos, ConstraintSolver.solve(env, CarID.fromID(id), a, this));
								} else {
									writeError(oos, "Cannot negotiate a timetable when the environment has not been set.");								
								}
							}
						} else if (ic == InformContent.REMOVE_PROPAGATE || ic == InformContent.REMOVE) {
							int id = ois.readInt();
							{
								CarID cid = CarID.fromID(id);
								if (env != null && env.hasCar(cid)) {
									env.removeCar(cid);
								}
							}
							if (ic == InformContent.REMOVE_PROPAGATE) {
								ACLMessage msg2 = Output.create(a, env.getAllCars().keySet());
								Output.write(msg2, oos2 -> {
									oos2.writeObject(InformContent.REMOVE);
									oos2.writeInt(id);
								});
								a.send(msg2);
								Output.awaitResponses(a, env.getAllCars().size() - 1);
							}
							oos.writeBoolean(false);
						} else if (ic == InformContent.GET_SWAP_DATA_SEND) {
							oos.writeObject(InformContent.GET_SWAP_DATA_RESULT);
							ImmutableCar car = env.getCar(CarID.fromID(id));
							oos.writeObject(new ConstraintSolver.AgentSwapData(car.getChargePerHour(), car.getUnavailableTimes()));
						} else if (ic == InformContent.NEXT_NEGOTIATION_SEND) {
							oos.writeObject(InformContent.NEXT_NEGOTIATION_RESULT);
							double mgl = (double)ois.readObject();
							Set<AID> sc = (Set<AID>)ois.readObject();
							Map<CarID, List<Integer>> tt = (Map<CarID, List<Integer>>)ois.readObject();
							double[] sl = (double[])ois.readObject();
							Integer[] li = (Integer[])ois.readObject();
							ConstraintSolver next = new ConstraintSolver(mgl, env.getCar(CarID.fromID(id)), a, this, sc, tt, sl, li);
							oos.writeObject(next.get());
						} else if (ic == InformContent.GET_SWAP_DATA_RESULT || ic == InformContent.NEXT_NEGOTIATION_RESULT) {
							returnListener.accept(ois);
						} else {
							writeError(oos, "Unknown inform content " + ic.toString());
						}
					});
				} catch (IOException | ClassNotFoundException | RuntimeIOException | RuntimeFIPAException | RuntimeClassNotFoundException e) {
					e.printStackTrace();
					writeError(oos, e.getMessage());
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			ran = false;
		}
		if (ran) {
			System.out.println("Sending reply to " + msg.getSender().getLocalName());
			a.send(response);
		}
	}
}
