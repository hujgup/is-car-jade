package cos30018.assignment.data;

import java.io.Serializable;
import java.time.LocalTime;
import cos30018.assignment.utils.LocalTimeRange;
import cos30018.assignment.utils.Validate;

/**
 * Represents an entry in a charging timetable.
 * 
 * @author Jake
 */
public class TimetableEntry implements Serializable {
	private static final long serialVersionUID = 8041101655729567522L;
	private Car car;
	private LocalTimeRange range;
	/**
	 * Creates a new TimetableEntry.
	 * 
	 * @param car The car this entry pertains to.
	 * @param startTime The time that charging will begin.
	 * @param endTime The time that charging will end.
	 */
	public TimetableEntry(Car car, LocalTime startTime, LocalTime endTime) {
		Validate.notNull(car, "car");
		Validate.notNull(startTime, "startTime");
		Validate.notNull(endTime, "endTime");
		this.car = car;
		this.range = new LocalTimeRange(startTime, true, endTime, false);
	}
	/**
	 * @return The car this entry pertains to.
	 */
	public Car getCar() {
		return car;
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
}
