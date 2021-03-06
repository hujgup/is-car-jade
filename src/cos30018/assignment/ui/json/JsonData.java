package cos30018.assignment.ui.json;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.chocosolver.util.tools.ArrayUtils;
import com.google.gson.annotations.SerializedName;
import cos30018.assignment.data.Car;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;
import cos30018.assignment.utils.LocalTimeRange;

/**
 * Data in a JSON object sent by the UI layer.
 * 
 * @author Jake
 */
public class JsonData {
	private static enum SpecType {
		EMPTY,
		ACTION,
		U_TIMES,
		NEW_CAR
	}	
	
	private static final Pattern TIME_REGEX = Pattern.compile("(?:[01]\\d|2[0-3])(?:\\:[0-5]\\d){1,2}");
	private Action action;
	private String id;
	private Double maxGridLoad;
	private Double currentCharge;
	private Double chargeCapacity;
	private Double chargePerHour;
	private Double chargeDrainPerHour;
	private List<LocalTimeRangeJson> unavailableTimes;
	private JsonData() {
	}
	/**
	 * Creates JsonData for a constraint update. All car constraints pertain to whatever car corresponds to the Agent receiving this data, or this agent if it's being returned to the UI layer.
	 * 
	 * @param maxGridLoad The maximum load on the electric grid at any one time.
	 * @param currentCharge The current charge of a car.
	 * @param chargeCapacity The maximum charge a car can have.
	 * @param chargePerHour The charge rate of a car.
	 * @param chargeDrainPerHour The charge drain rate of a car.
	 * @param unavailableTimes The times a car cannot be charged.
	 * @return JsonData representing a constraint update.
	 */
	public static JsonData createConstraintUpdate(Double maxGridLoad, Double currentCharge, Double chargeCapacity, Double chargePerHour, Double chargeDrainPerHour, List<LocalTimeRange> unavailableTimes) {
		JsonData res = new JsonData();
		res.action = Action.UPDATE_CONSTRAINTS;
		res.maxGridLoad = maxGridLoad;
		res.currentCharge = currentCharge;
		res.chargeCapacity = chargeCapacity;
		res.chargePerHour = chargePerHour;
		res.chargeDrainPerHour = chargeDrainPerHour;
		res.unavailableTimes = unavailableTimes.stream().map(ut -> ut.toJson()).collect(Collectors.toList());
		return res;
	}
	/**
	 * Creates JsonData for a negotiation force.
	 * 
	 * @return JsonData representing a negotiation forcing.
	 */
	public static JsonData createNegotiateCommand() {
		JsonData res = new JsonData();
		res.action = Action.FORCE_NEGOTIATE;
		return res;
	}
	/**
	 * Creates JsonData from a JSON string.
	 * 
	 * @param json The JSON string.
	 * @return JsonData from the specified JSON string.
	 */
	public static JsonData fromJson(String json) {
		return Json.deserialize(json, JsonData.class);
	}
	/*
	 * @return The action this object represents.
	 */
	public Action getAction() {
		return action;
	}
	/**
	 * @return True if this object represents a constraint update.
	 */
	public boolean isConstraintUpdate() {
		return action == Action.UPDATE_CONSTRAINTS;
	}
	/**
	 * @return True if this object represents a constraint update for a car.
	 */
	public boolean isCarConstraintUpdate() {
		return isConstraintUpdate() && (currentCharge != null || chargeCapacity != null || chargePerHour != null || chargeDrainPerHour != null || unavailableTimes != null);
	}
	/**
	 * @return True if this object represents a negotiation forcer.
	 */
	public boolean isNegotiateCommand() {
		return action == Action.FORCE_NEGOTIATE;
	}
	public boolean isCarGet() {
		return action == Action.GET_CARS;
	}
	public boolean isCarAdd() {
		return action == Action.ADD_CAR;		
	}
	public boolean isCarRemove() {
		return action == Action.REMOVE_CAR;		
	}
	public String getID() {
		return id;
	}
	/**
	 * @return The maxGridLoad value, or null if it should remain unchanged.
	 */
	public Double getMaxGridLoad() {
		return maxGridLoad;
	}
	/**
	 * @return The currentCharge value, or null if it should remain unchanged.
	 */
	public Double getCurrentCharge() {
		return currentCharge;
	}
	/**
	 * @return The chargeCapacity value, or null if it should remain unchanged.
	 */
	public Double getChargeCapacity() {
		return chargeCapacity;
	}
	/**
	 * @return The chargePerHour value, or null if it should remain unchanged.
	 */
	public Double getChargePerHour() {
		return chargePerHour;
	}
	/**
	 * @return The chargeDrainPerHour value, or null if it should remain unchanged.
	 */
	public Double getChargeDrainPerHour() {
		return chargeDrainPerHour;
	}
	/**
	 * @return The unavailableTimes value, or null if it should remain unchanged.
	 */
	public List<LocalTimeRangeJson> getUnavailableTimes() {
		return unavailableTimes;
	}
	/**
	 * Makes sure that a variable is specified.
	 * 
	 * @param value The variable that must be specified.
	 * @param key The JSON key corresponding to value.
	 * @param st Controls the postfix of the error message.
	 */
	private void mustSpecify(Object value, String key, SpecType st) {
		if (value == null) {
			String msg = "Key \"" + key + "\" must be specified";
			if (st != SpecType.EMPTY) {
				msg += " when \"action\" is " + action;
				if (st == SpecType.U_TIMES) {
					msg += " and \"unavailableTimes\" is specified";					
				} else if (st == SpecType.NEW_CAR) {
					msg += " and validation is assuming that this is a new car.";					
				}
			}
			msg += ".";
			throw new IllegalStateException(msg);
		}
	}
	/**
	 * Makes sure that a variable is not specified.
	 * 
	 * @param value The variable that should not be specified.
	 * @param key The JSON key corresponding to value.
	 */
	private void neverSpecify(Object value, String key) {
		if (value != null) {
			throw new IllegalStateException("Key \"" + key + "\" must not be specified when \"action\" is " + action + "\".");
		}
	}
	/**
	 * Makes sure that a variable is a time string.
	 * 
	 * @param str The variable that should be a time string.
	 * @param key The JSON key corresponding to value.
	 */
	private void isTimeString(String str, String key) {
		if (!TIME_REGEX.matcher(str).matches()) {
			throw new IllegalStateException("Key \"" + key + "\" must be a time string (matching pattern " + TIME_REGEX.pattern() + ") when \"action\" is " + action + " and \"unavailableTimes\" is specified");
		}
	}
	/**
	 * Makes sure that the data in this object makes sense (call this if this object was created with the fromJson method).
	 * 
	 * @param isNew True if this data specifies a new car.
	 */
	public void validate(Environment data, CarID cid, Action... allowedActions) {
		mustSpecify(action, "action", SpecType.EMPTY);
		if (!ArrayUtils.contains(allowedActions, action)) {
			throw new IllegalStateException("Action \"" + action + "\" is not allowed in this context.");
		} else if (isConstraintUpdate()) {
			neverSpecify(id, "id");
			if (!data.hasCar(cid)) {
				if (!isCarConstraintUpdate()) {
					throw new IllegalStateException("Validation is assuming that this is a new car, but some constraints were undefined.");
				}
				mustSpecify(maxGridLoad, "maxGridLoad", SpecType.NEW_CAR);
				mustSpecify(currentCharge, "currentCharge", SpecType.NEW_CAR);
				mustSpecify(chargeCapacity, "chargeCapacity", SpecType.NEW_CAR);
				mustSpecify(chargePerHour, "chargePerHour", SpecType.NEW_CAR);
				mustSpecify(chargeDrainPerHour, "chargeDrainPerHour", SpecType.NEW_CAR);
				mustSpecify(unavailableTimes, "unavailableTimes", SpecType.NEW_CAR);
			} else if (maxGridLoad == null && currentCharge == null && chargeCapacity == null && chargePerHour == null && chargeDrainPerHour == null && unavailableTimes == null) {
				throw new IllegalStateException("At least one constraint update must be specified when \"action\" is " + action + ".");
			}
			if (maxGridLoad != null && maxGridLoad <= 0) {
				throw new IllegalStateException("Key \"maxGridLoad\" must be positive.");
			} else if (currentCharge != null && currentCharge < 0) {
				throw new IllegalStateException("Key \"currentCharge\" cannot be negative.");
			} else if (chargeCapacity != null && chargeCapacity < 0) {
				throw new IllegalStateException("Key \"chargeCapacity\" cannot be negative.");				
			} else if (chargePerHour != null && chargePerHour <= 0) {
				throw new IllegalStateException("Key \"chargePerHour\" must be positive.");
			} else if (chargeDrainPerHour != null && chargeDrainPerHour < 0) {
				throw new IllegalStateException("Key \"chargeDrainPerHour\" cannot be negative.");
			} else if (unavailableTimes != null) {
				int i = 0;
				String keyStart;
				for (LocalTimeRangeJson ltrj : unavailableTimes) {
					keyStart = "unavailableTimes[" + i + "].";
					mustSpecify(ltrj.getLowerBound(), keyStart + "lowerBound", SpecType.U_TIMES);
					mustSpecify(ltrj.getUpperBound(), keyStart + "upperBound", SpecType.U_TIMES);
					mustSpecify(ltrj.getLowerBound().getPivot(), keyStart + "lowerBound.value", SpecType.U_TIMES);
					mustSpecify(ltrj.getLowerBound().getInclusive(), keyStart + "lowerBound.inclusive", SpecType.U_TIMES);
					mustSpecify(ltrj.getLowerBound().getPivot(), keyStart + "upperBound.value", SpecType.U_TIMES);
					mustSpecify(ltrj.getLowerBound().getInclusive(), keyStart + "upperBound.inclusive", SpecType.U_TIMES);
					isTimeString(ltrj.getLowerBound().getPivot(), keyStart + "lowerBound.value");
					isTimeString(ltrj.getUpperBound().getPivot(), keyStart + "upperBound.value");
					i++;
				}
			}
		} else if (isNegotiateCommand() || isCarAdd() || isCarRemove() || isCarGet()) {
			neverSpecify(maxGridLoad, "maxGridLoad");
			neverSpecify(currentCharge, "currentCharge");
			neverSpecify(chargeCapacity, "chargeCapacity");
			neverSpecify(chargePerHour, "chargePerHour");
			neverSpecify(chargeDrainPerHour, "chargeDrainPerHour");
			neverSpecify(unavailableTimes, "unavailableTimes");
			if (isCarRemove()) {
				mustSpecify(id, "id", SpecType.ACTION);
			} else {
				neverSpecify(id, "id");
			}
		} else {
			throw new IllegalStateException("Invalid \"action\" value: " + action + ".");
		}
	}
	/**
	 * Updates the given environment with changes contained in this object. Values on this object that are null incicate no change.
	 * 
	 * @param data The environment data to change.
	 * @param id The ID of the car to update, if this is a car constraint update.
	 */
	public void updateEnvironment(Environment data, CarID id) {
		if (isConstraintUpdate()) {
			if (getMaxGridLoad() != null) {
				data.setMaxGridLoad(getMaxGridLoad());
			}
			if (isCarConstraintUpdate()) {
				boolean isNewCar = !data.hasCar(id);
				Car car = isNewCar ? new Car(id.getID(), 0, Double.MIN_VALUE, Double.MIN_VALUE, 0) : data.getCar(id);
				if (isNewCar) {
					id = car.getOwner();
					data.addCar(car);
				}
				if (getCurrentCharge() != null) {
					car.setCurrentCharge(getCurrentCharge());
				}
				if (getChargeCapacity() != null) {
					car.setChargeCapacity(getChargeCapacity());
				}
				if (getChargePerHour() != null) {
					car.setChargePerHour(getChargePerHour());
				}
				if (getUnavailableTimes() != null) {
					car.setUnavailableTimes(getUnavailableTimes().stream().map(x -> x.toObject()).collect(Collectors.toList()));
				}
			}
		} else {
			throw new IllegalStateException("Cannot update constraints when contained data is not a constraint update.");
		}
	}
}
