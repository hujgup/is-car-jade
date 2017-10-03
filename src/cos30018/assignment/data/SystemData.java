package cos30018.assignment.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import cos30018.assignment.utils.Validate;

/**
 * Represents data relevant to the entire agent system.
 * 
 * @author Jake
 */
public class SystemData {
	private double maxGridLoad;
	private HashMap<CarID, Car> cars;
	private Timetable timetable;
	/**
	 * Creates a new SystemData object.
	 * 
	 * @param maxGridLoad The maximum load on the electric grid at any one time.
	 * @param cars The set of cars in this environment.
	 */
	public SystemData(double maxGridLoad, Set<Car> cars) {
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
	}
	private void setMaxGridLoad(double value, String argName) {
		Validate.finite(value, argName);
		Validate.positive(value, argName);
		maxGridLoad = value;
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
}
