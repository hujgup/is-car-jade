package cos30018.assignment.logic.scheduling;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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
import jade.domain.AMSService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;

public class ConstraintSolver implements Supplier<Map<CarID, List<Integer>>> {
	private static class AsdConsumer implements Consumer<ObjectInputStream> {
		// Getting around Java's weird "can't assign to local variables inside a lambda" thing by making result a field instead
		private Semaphore sync;
		public AgentSwapData res;
		public AsdConsumer(Semaphore sync) {
			this.sync = sync;
		}
		@SuppressWarnings("unchecked")
		@Override
		public void accept(ObjectInputStream x) {
			try {
				double gridLoad = x.readDouble();
				List<LocalTimeRange> uTimes = (List<LocalTimeRange>)x.readObject();
				res = new AgentSwapData(gridLoad, uTimes);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			sync.release();
		}		
	}

	private static class CarData {
		public CarID id;
		public int requiredChargeTime;
		private double gridLoad;
		public List<LocalTimeRange> unavailableTimes;
		public CarData(ImmutableCar car) {
			id = car.getOwner();
			requiredChargeTime = (int)Math.ceil((car.getChargeCapacity() - car.getCurrentCharge())/car.getChargePerHour());
			gridLoad = car.getChargePerHour();
			unavailableTimes = car.getUnavailableTimes();
		}
	}
	
	public static class AgentSwapData {
		private double gridLoad;
		private List<LocalTimeRange> uTimes;
		public AgentSwapData(double gl, List<LocalTimeRange> uts) {
			gridLoad = gl;
			uTimes = uts;
		}
		public double getGridLoad() {
			return gridLoad;
		}
		public List<LocalTimeRange> getUnavailableTimes() {
			return uTimes;
		}
	}
	
	// Sorts lowIndices first by slotLoads[lowIndices[i]] value, then by lowIndices[i]
	private Comparator<Integer> indexCmp = new Comparator<Integer>() {
		@Override
		public int compare(Integer a, Integer b) {
			int res = Double.compare(slotLoads[a], slotLoads[b]);
			if (res == 0) {
				res = Integer.compare(a, b);
			}
			return res;
		}
	};
	private static final int HOURS_IN_DAY = 24;
	private double maxGridLoad;
	private CarData selfCar;
	private Agent a;
	private ListenerBehaviour lb;
	private Set<AID> seenCars;
	private Map<CarID, List<Integer>> timetable;
	private double[] slotLoads;
	private Integer[] lowIndices;
	public ConstraintSolver(double maxGridLoad, ImmutableCar selfCar, Agent a, ListenerBehaviour lb, Set<AID> seenCars, Map<CarID, List<Integer>> timetable, double[] slotLoads, Integer[] lowIndices) {
		this.maxGridLoad = maxGridLoad;
		this.selfCar = new CarData(selfCar);
		this.a = a;
		this.lb = lb;
		this.seenCars = seenCars;
		this.timetable = timetable;
		this.slotLoads = slotLoads;
		this.lowIndices = lowIndices;
	}
	public ConstraintSolver(double maxGridLoad, ImmutableCar selfCar, Agent a, ListenerBehaviour lb) {
		this(maxGridLoad, selfCar, a, lb, new HashSet<>(), new HashMap<>(), createSlotLoads(), createLowIndices());
	}
	private static double[] createSlotLoads() {
		double[] res = new double[HOURS_IN_DAY];
		Arrays.fill(res, 0);
		return res;
	}
	private static Integer[] createLowIndices() {
		Integer[] res = new Integer[HOURS_IN_DAY];
		for (int i = 0; i < HOURS_IN_DAY; i++) {
			res[i] = i;
		}
		return res;
	}
	public static Map<CarID, List<Integer>> solve(Environment env, CarID self, Agent a, ListenerBehaviour lb) {
		return new ConstraintSolver(env.getMaxGridLoad(), env.getCar(self), a, lb).get();
	}
	private boolean isValidTime(List<LocalTimeRange> unavailableTimes, int hour) {
		boolean res = true;
		LocalTime ltHour = LocalTime.of(hour, 0);
		//System.out.println(ltHour.getHour());
		for (LocalTimeRange range : unavailableTimes) {
			//System.out.println(range.getLowerBound().getPivot().getHour() + ", " + range.getUpperBound().getPivot().getHour());
			res = range.argNotInRange(ltHour);
			if (!res) {
				break;
			}
		}
		//System.out.println(res);
		return res;
	}
	private void communicate(Consumer<ObjectInputStream> callback, boolean async, InformContent ic, AID target, Object... args) throws IOException, ClassNotFoundException {
		ACLMessage msg = Output.create(target);
		Output.write(msg, oos -> {
			oos.writeObject(ic);
			for (Object arg : args) {
				oos.writeObject(arg);
			}			
		});
		if (async) {
			lb.setReturnListener(callback);
		}
		a.send(msg);
		if (!async) {
			ACLMessage response = a.blockingReceive();
			Input.read(response, ois -> {
				@SuppressWarnings("unused")
				InformContent ic2 = (InformContent)ois.readObject();
				callback.accept(ois);
			});
		}
	}
	private AgentSwapData getSwapData(AID aid) throws IOException, ClassNotFoundException {
		Semaphore sync = new Semaphore(0);
		AsdConsumer c = new AsdConsumer(sync);
		communicate(c, true, InformContent.GET_SWAP_DATA_SEND, aid);
		sync.acquireUninterruptibly();
		return c.res;
	}
	private List<AID> getAllSchedulers() throws FIPAException {
		SearchConstraints c = new SearchConstraints();
		c.setMaxResults(-1L);
		return Arrays.stream(AMSService.search(a, new AMSAgentDescription(), c)).filter(x -> !x.getName().equals(selfCar) && x.getName().getLocalName().startsWith("sch")).map(x -> x.getName()).collect(Collectors.toList());
	}
	@SuppressWarnings("unchecked")
	private void sendToNextAgent() throws IOException, FIPAException, ClassNotFoundException {
		List<AID> allSchedulers = getAllSchedulers();
		System.out.println(allSchedulers.size());
		System.out.println(seenCars.size());
		for (AID x : seenCars) {
			System.out.println(x.getLocalName());
		}
		if (allSchedulers.size() > seenCars.size()) {
			AID aid = null;
			for (AID x : allSchedulers) {
				if (!seenCars.contains(x)) {
					aid = x;
					break;
				}
			}
			Semaphore sync = new Semaphore(0);
			communicate(x -> {
				try {
					timetable = (Map<CarID, List<Integer>>)x.readObject();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				sync.release();
			}, false, InformContent.NEXT_NEGOTIATION_SEND, aid, maxGridLoad, seenCars, timetable, slotLoads, lowIndices);
			sync.acquireUninterruptibly();
		}
	}
	@Override
	public Map<CarID, List<Integer>> get() {
		boolean isFirst = seenCars.isEmpty();
		if (isFirst) {
			System.out.println("Solving...");
		}
		System.out.println("Solve tick for " + selfCar.id.getID() + " starting...");
		// Constraints:
		// Must provide cd.requiredChargeTime slots
		// Cannot take a slot if it overlaps with cd.unavailableTimes
		// One car cannot have more than one slot for the same hour
		// Sum of all charge draws for a given hour cannot exceed maxGridLoad
		
		try {
			CarData cd = selfCar;
			ArrayList<Integer> selfTt = new ArrayList<>();
			timetable.put(cd.id, selfTt);
			double sum;
			int slot;
			int i;
			for (i = 0; i < lowIndices.length; i++) {
				slot = lowIndices[i];
				if (isValidTime(cd.unavailableTimes, slot)) {
					//System.out.println(slot + " is valid time");
					sum = slotLoads[slot] + cd.gridLoad;
					if (sum <= maxGridLoad) {
						//System.out.println("Using slot " + slot + " would not exceed max grid load");
						selfTt.add(slot);
						slotLoads[slot] = sum;
						//System.out.println("Now has " + selfTt.size() + " slots (out of " + cd.requiredChargeTime + " needed)");
						if (selfTt.size() >= cd.requiredChargeTime) {
							// All slots fulfilled - no point checking for more
							//System.out.println("Car " + cd.id.getID() + " success");
							break;
						}
					}
				}
			}
			if (selfTt.size() < cd.requiredChargeTime) {
				List<Integer> cd2CanSwapTo;
				List<Integer> cdCanSwapTo;
				int needed;
				int min;
				int j;
				for (AID aid : seenCars) {
					final AgentSwapData sd = getSwapData(aid);
					final List<Integer> otherTt = timetable.get(aid);
					cdCanSwapTo = Arrays.stream(lowIndices).filter(x -> !selfTt.contains(x) && otherTt.contains(x) && isValidTime(cd.unavailableTimes, x) && slotLoads[x] - sd.gridLoad + cd.gridLoad <= maxGridLoad).collect(Collectors.toList());
					cd2CanSwapTo = Arrays.stream(lowIndices).filter(x -> !selfTt.contains(x) && !otherTt.contains(x) && isValidTime(sd.uTimes, x) && slotLoads[x] - cd.gridLoad + sd.gridLoad <= maxGridLoad).collect(Collectors.toList());
					needed = cd.requiredChargeTime - selfTt.size();
					min = Math.min(needed, cd2CanSwapTo.size());
					for (i = 0; i < min; i++) {
						j = cdCanSwapTo.get(i);
						selfTt.add(j);
						otherTt.set(j, cd2CanSwapTo.get(i));
					}
					if (selfTt.size() >= cd.requiredChargeTime) {
						break;
					}
				}
				if (selfTt.size() < cd.requiredChargeTime) {
					System.out.println(cd.id.getID() + " => Constraints cannot be exactly satisfied. Best fit solution provided.");													
				}
			}
			// Sort lowIndices first by slotLoads[lowIndices[i]] value, then by lowIndices[i]
			// This ensures that slots with the lowest grid utilization are taken first by the next car
			Arrays.sort(lowIndices, indexCmp);
			seenCars.add(selfCar.id.getScheduler());
			System.out.println("Solve tick for " + selfCar.id.getID() + " done.");
			sendToNextAgent();
			if (isFirst) {
				System.out.println("Solved.");
			}
			return timetable;
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		} catch (FIPAException e) {
			throw new RuntimeFIPAException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeClassNotFoundException(e);
		}
	}
}
