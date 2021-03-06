package cos30018.assignment.ui.json;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import com.google.gson.annotations.SerializedName;
import cos30018.assignment.utils.Bound;
import cos30018.assignment.utils.Validate;

/**
 * Represents a Bound<LocalTime> in a JSON-convertible form.
 * 
 * @author Jake
 */
public class LocalTimeBoundJson {
	@SerializedName("value")
	private String pivot;
	private boolean inclusive;
	/**
	 * Creates a new LocalTimeBoundJson.
	 * 
	 * @param pivot The pivot value.
	 * @param inclusive True if the bound is inclusive.
	 */
	public LocalTimeBoundJson(LocalTime pivot, boolean inclusive) {
		Validate.notNull(pivot, "pivot");
		this.pivot = pivot.format(DateTimeFormatter.ISO_LOCAL_TIME);
		this.inclusive = inclusive;
	}
	private LocalTimeBoundJson() {
	}
	/**
	 * @return The pivot time as a time string.
	 */
	public String getPivot() {
		return pivot;
	}
	/**
	 * @return True if this bound is inclusive.
	 */
	public boolean getInclusive() {
		return inclusive;
	}
	/**
	 * @return This JSON object as its corresponding Java object.
	 */
	public Bound<LocalTime> toObject() {
		return new Bound<>(LocalTime.parse(pivot), inclusive);
	}
}
