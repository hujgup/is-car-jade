package cos30018.assignment.logic;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;
import cos30018.assignment.data.ImmutableCar;
import cos30018.assignment.utils.LocalTimeRange;

public class ConstraintSolver implements Supplier<Map<CarID, List<Integer>>> {
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
	
	private double maxGridLoad;
	private CarData[] cars;
	public ConstraintSolver(Environment env) {
		maxGridLoad = env.getMaxGridLoad();
		cars = new CarData[env.getAllCars().size()];
		int i = 0;
		for (ImmutableCar car : env.getAllCars().values()) {
			cars[i++] = new CarData(car);
		}
	}
	public static Map<CarID, List<Integer>> solve(Environment env) {
		return new ConstraintSolver(env).get();
	}
	private boolean isValidTime(CarData cd, int hour) {
		boolean res = true;
		LocalTime ltHour = LocalTime.of(hour, 0);
		System.out.println(ltHour.getHour());
		for (LocalTimeRange range : cd.unavailableTimes) {
			System.out.println(range.getLowerBound().getPivot().getHour() + ", " + range.getUpperBound().getPivot().getHour());
			res = range.argNotInRange(ltHour);
			if (!res) {
				break;
			}
		}
		System.out.println(res);
		return res;
	}
	@Override
	public Map<CarID, List<Integer>> get() {
		System.out.println("Solving...");
		// Constraints:
		// Must provide cd.requiredChargeTime slots
		// Cannot take a slot if it overlaps with cd.unavailableTimes
		// One car cannot have more than one slot for the same hour
		// Sum of all charge draws for a given hour cannot exceed maxGridLoad
		
		System.out.println("Populating variables...");
		double[] slotLoads = new double[24];
		Integer[] lowIndices = new Integer[slotLoads.length];
		Arrays.fill(slotLoads, 0);
		for (int i = 0; i < lowIndices.length; i++) {
			lowIndices[i] = i;
		}
		// Sorts lowIndices first by slotLoads[lowIndices[i]] value, then by lowIndices[i]
		Comparator<Integer> indexCmp = new Comparator<Integer>() {
			@Override
			public int compare(Integer a, Integer b) {
				int res = Double.compare(slotLoads[a], slotLoads[b]);
				if (res == 0) {
					res = Integer.compare(a, b);
				}
				return res;
			}
		};
		Map<CarID, List<Integer>> timetable = new HashMap<>();
		ArrayList<Integer> selfTt;
		double sum;
		int slot;
		int i;
		for (CarData cd : cars) {
			System.out.println("Car " + cd.id.getID());
			selfTt = new ArrayList<>();
			timetable.put(cd.id, selfTt);
			for (i = 0; i < lowIndices.length; i++) {
				slot = lowIndices[i];
				if (isValidTime(cd, slot)) {
					System.out.println(slot + " is valid time");
					sum = slotLoads[slot] + cd.gridLoad;
					if (sum <= maxGridLoad) {
						System.out.println("Using slot " + slot + " would not exceed max grid load");
						selfTt.add(slot);
						slotLoads[slot] = sum;
						System.out.println("Now has " + selfTt.size() + " slots (out of " + cd.requiredChargeTime + " needed)");
						if (selfTt.size() >= cd.requiredChargeTime) {
							// All slots fulfilled - no point checking for more
							System.out.println("Car " + cd.id.getID() + " success");
							break;
						}
					}
				}
			}
			if (selfTt.size() < cd.requiredChargeTime) {
				// Unresolvable - no car takes more than it needs and always minimizes any one slot's grid load
				System.out.println(cd.id.getID() + " => Constraints cannot be exactly satisfied. Best fit solution provided.");
			}
			// Sort lowIndices first by slotLoads[lowIndices[i]] value, then by lowIndices[i]
			// This ensures that slots with the lowest grid utilization are taken first by the next car
			Arrays.sort(lowIndices, indexCmp);
		}
		System.out.println("Solved.");
		return timetable;
	}
}
