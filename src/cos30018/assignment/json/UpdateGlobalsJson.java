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
public class UpdateGlobalsJson extends JsonUpdater {
	private static final Method SET_MGLOAD;
	static {
		try {
			SET_MGLOAD = SystemData.class.getMethod("setMaxGridLoad", double.class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	// All these primitives are boxed because they could be omitted (set to null by Gson).
	private Double maxGridLoad;
	private List<UpdateCarJson> cars;
	private UpdateGlobalsJson() {
	}
	/**
	 * Transmutes a JSON string into an UpdateGlobalsJson object.
	 * 
	 * @param json The JSON string.
	 * @return A ClientRequest containing the data inside the JSON string.
	 */
	public static UpdateGlobalsJson fromJson(String json) {
		return Provider.OBJ.fromJson(json, UpdateGlobalsJson.class);
	}
	/**
	 * @return The new max grid load, or null if it should remain unchanged.
	 */
	public Double getMaxGridLoad() {
		return maxGridLoad;
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
			res |= updateField(maxGridLoad, SET_MGLOAD, data);
			res |= update(data);
			/*
			res |= updateField(currentCharge, SET_CCUR, car);
			res |= updateField(chargeCapacity, SET_CCAP, car);
			res |= updateField(chargePerHour, SET_CRATE, car);
			res |= updateField(unavailableTimes, SET_UATIMES, car);
			res |= updateField(negotiationOrder, SET_NORD, car);
			*/
			return res;
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			// This should never happen on release because reflection is not dependent on user input.
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
