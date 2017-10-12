package cos30018.assignment.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import cos30018.assignment.ui.json.JsonData;
import cos30018.assignment.utils.Validate;

/**
 * Represents data relevant to the entire agent system.
 * 
 * @author Jake
 */
public class Environment {
	private boolean isDummy;
	private double maxGridLoad;
	private HashMap<CarID, Car> cars;
	private Timetable timetable;
	/**
	 * Creates a new Environment.
	 * 
	 * @param maxGridLoad The maximum load on the electric grid at any one time.
	 * @param cars The set of cars in this environment.
	 */
	public Environment(double maxGridLoad, Set<Car> cars) {
		setMaxGridLoad(maxGridLoad, "maxGridLoad");
		Validate.notNull(cars, "cars");
		this.cars = new HashMap<>();
		int i = 0;
		for (Car car : cars) {
			Validate.notNull(car, "cars[" + i + "]");
			this.cars.put(car.getOwner(), car);
			i++;
		}
		timetable = new Timetable();
		isDummy = false;
	}
	/**
	 * Creates a new Environment.
	 * 
	 * @param maxGridLoad The maximum load on the electric grid at any one time.
	 * @param cars The set of cars in this environment.
	 */
	public Environment(double maxGridLoad, Car... cars) {
		this(maxGridLoad, toSet(cars));
	}
	/**
	 * @return An Environment that contains placeholder data.
	 */
	public static Environment createDummyData() {
		Environment res = new Environment(Double.MIN_VALUE);
		res.isDummy = true;
		return res;
	}
	private static Set<Car> toSet(Car[] cars) {
		Set<Car> res = new HashSet<>();
		for (int i = 0; i < cars.length; i++) {
			if (!res.add(cars[i])) {
				throw new IllegalArgumentException("Array of cars cannot be mapped to aset because it contains duplicates.");
			}
		}
		return res;
	}
	private void setMaxGridLoad(double value, String argName) {
		Validate.finite(value, argName);
		Validate.positive(value, argName);
		maxGridLoad = value;
		isDummy = false;
	}
	/**
	 * @return True if this object was created using createDummyData() and has not been mutated since then.
	 */
	public boolean isDummy() {
		return isDummy;
	}
	/**
	 * @return The maximum load on the electric grid at any one time.
	 */
	public double getMaxGridLoad() {
		return maxGridLoad;
	}
	/**
	 * Sets the max grid load.
	 * 
	 * @param value The max grid load.
	 */
	public void setMaxGridLoad(double value) {
		setMaxGridLoad(value, "value");
	}
	/**
	 * Checks whether a car exists in this environment.
	 * 
	 * @param id The car's ID.
	 * @return True if the car is in this environment.
	 */
	public boolean hasCar(CarID id) {
		return cars.containsKey(id);
	}
	/**
	 * Gets the car associated with the give ID.
	 * 
	 * @param id The car's ID.
	 * @return The car associated with id, or null if none exists.
	 */
	public Car getCar(CarID id) {
		return cars.get(id);
	}
	/**
	 * Adds the specified car to the environment.
	 * 
	 * @param car The car to add.
	 * @return True if the car was added, false if that car is already in this environment.
	 */
	public boolean addCar(Car car) {
		boolean res = !hasCar(car.getOwner());
		if (res) {
			cars.put(car.getOwner(), car);
			isDummy = false;			
		}
		return res;
	}
	/**
	 * Removes a car from this environment.
	 * 
	 * @param id The car's ID.
	 * @return True if the car was removed.
	 */
	public boolean removeCar(CarID id) {
		return cars.remove(id) != null;
	}
	/**
	 * @return All cars in this environment.
	 */
	public Map<CarID, Car> getAllCars() {
		return Collections.unmodifiableMap(cars);
	}
	/**
	 * @return The timetable being created or updated by this environment.
	 */
	public Timetable getTimetable() {
		return timetable;
	}
	/**
	 * @param withCar The car to union with.
	 * @return This object as an object that can be converted to JSON, unioned with withCar.toJson().
	 */
	public JsonData toJson(Car withCar) {
		return JsonData.createConstraintUpdate(maxGridLoad, withCar.getCurrentCharge(), withCar.getChargeCapacity(), withCar.getChargePerHour(), withCar.getChargeDrainPerHour(), withCar.getUnavailableTimes());
	}
	/**
	 * @param withCar The ID of the car to union with.
	 * @return This object as an object that can be converted to JSON, unioned with withCar.toJson().
	 */
	public JsonData toJson(CarID withCar) {
		return toJson(getCar(withCar));
	}
	/**
	 * @return This object as an object that can be converted to JSON.
	 */
	public JsonData toJson() {
		return JsonData.createConstraintUpdate(maxGridLoad, null, null, null, null, null);
	}
}
