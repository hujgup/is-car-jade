package cos30018.assignment.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import cos30018.assignment.data.Car;
import cos30018.assignment.data.SystemData;
import cos30018.assignment.utils.LocalTimeRange;

/**
 * Represents the JSON data extracted from a request originating from a web client.
 * 
 * @author Jake
 */
public class ClientRequest {
	private static final Method SET_MGLOAD;
	private static final Method SET_CCUR;
	private static final Method SET_CCAP;
	private static final Method SET_CRATE;
	private static final Method SET_UATIMES;
	private static final Method SET_NORD;
	static {
		try {
			SET_MGLOAD = SystemData.class.getMethod("setMaxGridLoad", double.class);
			SET_CCUR = Car.class.getMethod("setCurrentCharge", double.class);
			SET_CCAP = Car.class.getMethod("setChargeCapacity", double.class);
			SET_CRATE = Car.class.getMethod("setChargePerHour", double.class);
			SET_UATIMES = Car.class.getMethod("setUnavailableTimes");
			SET_NORD = Car.class.getMethod("setNegotiationOrder", int.class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	// All these primitives are boxed because they could be omitted (set to null by Gson).
	private Double maxGridLoad;
	// TODO: Allow multiple cars to be updated at once (sub object containing most of this stuff, mapped by CarID)
	private Double currentCharge;
	private Double chargeCapacity;
	private Double chargePerHour;
	private List<LocalTimeRange> unavailableTimes;
	private Integer negotiationOrder;
	private ClientRequest() {
	}
	/**
	 * Transmutes a JSON string into a ClientRequest object.
	 * 
	 * @param json The JSON string.
	 * @return A ClientRequest containing the data inside the JSON string.
	 */
	public static ClientRequest fromJson(String json) {
		return Provider.OBJ.fromJson(json, ClientRequest.class);
	}
	private boolean updateField(Object thisValue, Method carFieldSetter, Object instance) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		boolean res = thisValue != null;
		if (res) {
			carFieldSetter.invoke(instance, thisValue);
		}
		return res;
	}
	/**
	 * @return The new max grid load, or null if it should remain unchanged.
	 */
	public Double getMaxGridLoad() {
		return maxGridLoad;
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
	 * Updates the specified Car object with the data in this ClientRequest. Fields that are null will retain the values inside the Car object.
	 * 
	 * @param car The Car to update.
	 * @return True if at least one field of the Car object was updated.
	 */
	public boolean update(SystemData data, Car car) {
		try {
			boolean res = false;
			res |= updateField(maxGridLoad, SET_MGLOAD, data);
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
