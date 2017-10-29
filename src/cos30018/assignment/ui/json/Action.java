package cos30018.assignment.ui.json;

import com.google.gson.annotations.SerializedName;

/**
 * Possible actions a JsonData object can represent.
 * 
 * @author Jake
 */
public enum Action {
	/**
	 * Updates all specified constraints.
	 */
	@SerializedName("constraints")
	UPDATE_CONSTRAINTS,
	/**
	 * Forces a new negotiation to begin.
	 */
	@SerializedName("negotiate")
	FORCE_NEGOTIATE,
	@SerializedName("getCars")
	GET_CARS,
	@SerializedName("addCar")
	ADD_CAR,
	@SerializedName("removeCar")
	REMOVE_CAR;
	@Override
	public String toString() {
		try {
			return this.getClass().getField(this.name()).getAnnotation(SerializedName.class).value();
		} catch (NoSuchFieldException | SecurityException e) {
			return "Action reflection error: " + e.getMessage();
		}
	}
}
