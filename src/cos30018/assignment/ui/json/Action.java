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
	FORCE_NEGOTIATE;
	@Override
	public String toString() {
		return this == UPDATE_CONSTRAINTS ? "constraints" : "negotiate";
	}
}
