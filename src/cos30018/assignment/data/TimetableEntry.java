package cos30018.assignment.data;

import java.io.Serializable;
import java.time.LocalTime;
import com.google.gson.annotations.SerializedName;
import cos30018.assignment.ui.json.JsonConvertible;
import cos30018.assignment.ui.json.LocalTimeRangeJson;
import cos30018.assignment.utils.LocalTimeRange;
import cos30018.assignment.utils.Validate;

/**
 * Represents an entry in a charging timetable.
 * 
 * @author Jake
 */
public class TimetableEntry implements JsonConvertible<TimetableEntry.Json>, Serializable {
	public class Json {
		@SerializedName("id")
		private int id2;
		@SerializedName("range")
		private LocalTimeRangeJson range2;
		private Json() {
			id2 = id.getID();
			range2 = range.toJson();
		}
	}
	
	private static final long serialVersionUID = 8041101655729567522L;
	private CarID id;
	private LocalTimeRange range;
	/**
	 * Creates a new TimetableEntry.
	 * 
	 * @param car The car this entry pertains to.
	 * @param startTime The time that charging will begin.
	 * @param endTime The time that charging will end.
	 */
	public TimetableEntry(CarID id, LocalTimeRange range) {
		Validate.notNull(id, "id");
		Validate.notNull(range, "range");
		this.id = id;
		this.range = range;
	}
	/**
	 * @return The car this entry pertains to.
	 */
	public CarID getId() {
		return id;
	}
	/**
	 * @return The temporal range this entry spans.
	 */
	public LocalTimeRange getTimeRange() {
		return range;
	}
	/**
	 * @return The time that charging begins (inclusive).
	 */
	public LocalTime getStartTime() {
		return range.getLowerBound().getPivot();
	}
	/**
	 * @return The time that charging ends (exclusive).
	 */
	public LocalTime getEndTime() {
		return range.getUpperBound().getPivot();
	}
	public Json toJson() {
		return new Json();
	}
}
