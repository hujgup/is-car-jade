package cos30018.assignment.utils;

import java.time.LocalTime;
import cos30018.assignment.ui.json.JsonConvertible;
import cos30018.assignment.ui.json.LocalTimeBoundJson;
import cos30018.assignment.ui.json.LocalTimeRangeJson;

/**
 * Represents a range of LocalTimes.
 * 
 * @author Jake
 */
public class LocalTimeRange implements JsonConvertible<LocalTimeRangeJson>, Range<LocalTimeRange, LocalTime> {
	private static final long serialVersionUID = -2818936802648211668L;
	static {
		SimpleRange.registerSpecialization(LocalTime.class);
	}
	private Bound<LocalTime> low;
	private Bound<LocalTime> high;
	private boolean overflowsDay;
	/**
	 * Creates a new LocalTimeRange.
	 * 
	 * @param lowerPivot The value of the lower bound.
	 * @param lowerInclusive Whether this range should include the lower bound or not.
	 * @param upperPivot The value of the upper bound.
	 * @param upperInclusive Whether this range should include the upper bound or not.
	 */
	public LocalTimeRange(LocalTime lowerPivot, boolean lowerInclusive, LocalTime upperPivot, boolean upperInclusive) {
		this(new Bound<>(lowerPivot, lowerInclusive), new Bound<>(upperPivot, upperInclusive));
	}
	/**
	 * Creates a new LocalTimeRange.
	 * 
	 * @param lowerPivot The value of the lower bound.
	 * @param lowerInclusive Whether this range should include the lower bound or not.
	 * @param upperBound The upper bound of this range.
	 */
	public LocalTimeRange(LocalTime lowerPivot, boolean lowerInclusive, Bound<LocalTime> upperBound) {
		this(new Bound<>(lowerPivot, lowerInclusive), upperBound);
	}
	/**
	 * Creates a new LocalTimeRange.
	 * 
	 * @param lowerBound The lower bound of this range.
	 * @param upperPivot The value of the upper bound.
	 * @param upperInclusive Whether this range should include the upper bound or not.
	 */
	public LocalTimeRange(Bound<LocalTime> lowerBound, LocalTime upperPivot, boolean upperInclusive) {
		this(lowerBound, new Bound<>(upperPivot, upperInclusive));
	}
	/**
	 * Creates a new LocalTimeRange.
	 * 
	 * @param lowerBound The lower bound of this range.
	 * @param upperBound The upper bound of this range.
	 */
	public LocalTimeRange(Bound<LocalTime> lowerBound, Bound<LocalTime> upperBound) {
		Validate.notNull(lowerBound, "lowerBound");
		Validate.notNull(upperBound, "upperBound");
		low = lowerBound;
		high = upperBound;
		overflowsDay = low.getPivot().compareTo(high.getPivot()) > 0;
	}
	@Override
	public Bound<LocalTime> getLowerBound() {
		return low;
	}
	@Override
	public Bound<LocalTime> getUpperBound() {
		return high;
	}
	/**
	 * @return True if this range crosses the day boundary (i.e. the lower bound is less than the upper bound).
	 */
	public boolean overflowsDay() {
		return overflowsDay;
	}
	@Override
	public boolean argInRange(LocalTime arg) {
		boolean res;
		if (overflowsDay) {
			// Can be either higher than low or lower than high
			res = !low.argIsBelow(arg) || !high.argIsAbove(arg);
		} else {
			// Must be between low and high
			res = !low.argIsBelow(arg) && !high.argIsAbove(arg);
		}
		return res;
	}
	@Override
	public boolean argNotInRange(LocalTime arg) {
		return !argInRange(arg);
	}
	@Override
	public boolean isInside(LocalTimeRange range) {
		boolean res;
		if (overflowsDay && !range.overflowsDay) {
			// Guaranteed false
			res = false;
		} else {
			if (range.low.isInclusive() || range.low.isInclusive() == low.isInclusive()) {
				// Standard pivot comparison
				res = range.argInRange(low.getPivot());
			} else {
				// Guaranteed false
				res = false;
			}
			if (res) {
				if (range.high.isInclusive() || range.high.isInclusive() == high.isInclusive()) {
					res = range.argInRange(high.getPivot());
				} else {
					res = false;
				}
			}
		}
		return res;
	}
	@Override
	public boolean isOutside(LocalTimeRange range) {
		return !isInside(range);
	}
	@Override
	public boolean overlaps(LocalTimeRange range) {
		return range.argInRange(low.getPivot()) || range.argInRange(high.getPivot());
	}
	@Override
	public String toString() {
		return Range.toString(this);
	}
	/**
	 * @return This object, but treated as a discreet range.
	 */
	public SimpleRange<Integer> toHourRange() {
		int lowPivot = low.getPivot().getHour();
		int highPivot = high.getPivot().getHour();
		if (!low.isInclusive()) {
			lowPivot++;
		}
		if (!high.isInclusive()) {
			highPivot--;
		}
		return new SimpleRange<>(lowPivot, true, highPivot, true);
	}
	/**
	 * @return This object as an object that can be converted to JSON.
	 */
	public LocalTimeRangeJson toJson() {
		LocalTimeBoundJson lowJson = new LocalTimeBoundJson(low.getPivot(), low.isInclusive());
		LocalTimeBoundJson highJson = new LocalTimeBoundJson(high.getPivot(), high.isInclusive());
		return new LocalTimeRangeJson(lowJson, highJson);
	}
}
