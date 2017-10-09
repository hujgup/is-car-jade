package cos30018.assignment.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import cos30018.assignment.json.JsonData;
import cos30018.assignment.utils.LocalTimeRange;
import cos30018.assignment.utils.Validate;

/**
 * Represents a car and its associated constraints.
 * 
 * @author Jake
 */
public class Car implements ImmutableCar {
	// IMPORTANT: Every time you add a new constraint field here, remember to:
	// 1. Add getters and setters to this object.
	// 2. Add the getter method to the ImmutableCar interface
	// 3. Consider whether it needs to be added to the Timetable.AddResult enum and the Timetable add method logic.
	// 4. Update JsonData so that it uses it.
	// 5. Update constraint negotiation to consider it.
	private CarID owner;
	private double currentCharge;
	private double chargeCapacity;
	private double chargePerHour;
	private List<LocalTimeRange> unavailableTimes;
	private int negotiationOrder;
	/**
	 * Creates a new car.
	 * 
	 * @param owner The ID of the agents that own this car.
	 * @param currentCharge The initial charge level of this car.
	 * @param chargeCapacity The maximum amount of charge this car can hold.
	 * @param chargePerHour The amount of charge this car can gain per hour while charging (controls charge rate).
	 * @param unavailableTimes The set of times that this car cannot charge in, e.g. if it is being used to drive to work.
	 */
	public Car(CarID owner, double currentCharge, double chargeCapacity, double chargePerHour, List<LocalTimeRange> unavailableTimes) {
		Validate.notNull(owner, "owner");
		this.owner = owner;
		setCurrentCharge(currentCharge, "currentCharge");
		setChargeCapacity(chargeCapacity, "chargeCapacity");
		setChargePerHour(chargePerHour, "chargePerHour");
		setUnavailableTimes(unavailableTimes, "unavailableTimes");
	}
	/**
	 * Creates a new car.
	 * 
	 * @param owner The ID of the agents that own this car.
	 * @param currentCharge The initial charge level of this car.
	 * @param chargeCapacity The maximum amount of charge this car can hold.
	 * @param chargePerHour The amount of charge this car can gain per hour while charging (controls charge rate).
	 * @param unavailableTimes The set of times that this car cannot charge in, e.g. if it is being used to drive to work.
	 */
	public Car(CarID owner, double currentCharge, double chargeCapacity, double chargePerHour, LocalTimeRange... unavailableTimes) {
		this(owner, currentCharge, chargeCapacity, chargePerHour, Arrays.asList(unavailableTimes));
	}
	private void setCurrentCharge(double value, String argName) {
		Validate.finite(value, argName);
		Validate.notNegative(value, argName);
		currentCharge = value;
	}
	private void setChargeCapacity(double value, String argName) {
		Validate.finite(value, argName);
		Validate.notNegative(value, argName);
		Validate.greaterThanOrEqualTo(value, currentCharge, argName);
		chargeCapacity = value;
	}
	private void setChargePerHour(double value, String argName) {
		Validate.finite(value, argName);
		Validate.positive(value, argName);
		chargePerHour = value;
	}
	private void setUnavailableTimes(List<LocalTimeRange> value, String argName) {
		Validate.notNull(value, argName);
		int i = 0;
		for (LocalTimeRange range : value) {
			Validate.notNull(range, argName + "[" + i + "]");
			i++;
		}
		unavailableTimes = Collections.unmodifiableList(value);
	}
	@Override
	public CarID getOwner() {
		return owner;
	}
	@Override
	public double getCurrentCharge() {
		return currentCharge;
	}
	/**
	 * Sets the current charge in this car.
	 * 
	 * @param value The charge.
	 */
	public void setCurrentCharge(double value) {
		setCurrentCharge(value, "value");
	}
	@Override
	public double getChargeCapacity() {
		return chargeCapacity;
	}
	/**
	 * Sets the charge capacity of this car.
	 * 
	 * @param value The charge capacity.
	 */
	public void setChargeCapacity(double value) {
		setCurrentCharge(value, "value");
	}
	@Override
	public double getChargePerHour() {
		return chargePerHour;
	}
	/**
	 * Sets the charge rate of this car.
	 * 
	 * @param value The charge rate.
	 */
	public void setChargePerHour(double value) {
		setChargePerHour(value, "value");
	}
	@Override
	public List<LocalTimeRange> getUnavailableTimes() {
		return unavailableTimes;
	}
	/**
	 * Sets the unavailable times of this car.
	 * 
	 * @param value The unavailable times.
	 */
	public void setUnavailableTimes(List<LocalTimeRange> value) {
		setUnavailableTimes(value, "value");
	}
	@Override
	public int getNegotiationOrder() {
		return owner.getID();
	}
	/**
	 * @return This object as an object that can be converted to JSON.
	 */
	public JsonData toJson() {
		return JsonData.createConstraintUpdate(null, currentCharge, chargeCapacity, chargePerHour, unavailableTimes);
	}
}
