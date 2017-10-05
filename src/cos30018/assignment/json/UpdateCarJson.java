package cos30018.assignment.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import cos30018.assignment.data.Car;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.SystemData;
import cos30018.assignment.utils.LocalTimeRange;

public class UpdateCarJson extends JsonUpdater {
	private static final Method SET_CCUR;
	private static final Method SET_CCAP;
	private static final Method SET_CRATE;
	private static final Method SET_UATIMES;
	private static final Method SET_NORD;
	static {
		try {
			SET_CCUR = Car.class.getMethod("setCurrentCharge", double.class);
			SET_CCAP = Car.class.getMethod("setChargeCapacity", double.class);
			SET_CRATE = Car.class.getMethod("setChargePerHour", double.class);
			SET_UATIMES = Car.class.getMethod("setUnavailableTimes");
			SET_NORD = Car.class.getMethod("setNegotiationOrder", int.class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	private int carId;
	private Double currentCharge;
	private Double chargeCapacity;
	private Double chargePerHour;
	private List<LocalTimeRange> unavailableTimes;
	private Integer negotiationOrder;
	private UpdateCarJson() {
	}
	/**
	 * @return The ID of the car this object pertains to.
	 */
	public CarID getCarID() {
		return CarID.fromID(carId);
	}
	/**
	 * @return The new current charge, or null if it should remain unchanged.
	 */
	public Double getCurrentCharge() {
		return currentCharge;
	}
	/**
	 * @return The new charge capacity, or null if it should remain unchanged.
	 */
	public Double getChargeCapacity() {
		return chargeCapacity;
	}
	/**
	 * @return The new charge rate, or null if it should remain unchanged.
	 */
	public Double getChargePerHour() {
		return chargePerHour;
	}
	/**
	 * @return The new set of unavailable times, or null if it should remain unchanged.
	 */
	public List<LocalTimeRange> getUnavailableTimes() {
		return unavailableTimes;
	}
	/**
	 * @return The new negotiation order, or null if it should remain unchanged.
	 */
	public Integer getNegotiationOrder() {
		return negotiationOrder;
	}
	/**
	 * Updates the environment with the data in this ClientRequest. Fields that are null will retain their current values.
	 * 
	 * @param data The system data to update.
	 * @return True if at least one field was updated.
	 */
	public boolean update(SystemData data) {
		try {
			boolean res = false;
			Car car = data.getCar(getCarID());
			res |= updateField(currentCharge, SET_CCUR, car);
			res |= updateField(chargeCapacity, SET_CCAP, car);
			res |= updateField(chargePerHour, SET_CRATE, car);
			res |= updateField(unavailableTimes, SET_UATIMES, car);
			res |= updateField(negotiationOrder, SET_NORD, car);
			return res;
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			// This should never happen on release because reflection is not dependent on user input.
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
