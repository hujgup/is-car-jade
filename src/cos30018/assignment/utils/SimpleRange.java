package cos30018.assignment.utils;

/**
 * Represents a range of values.
 * 
 * Specializations:
 * SimpleRange<LocalTime> => LocalTimeRange
 * 
 * @author Jake
 *
 * @param <T> The bounding type.
 */
public class SimpleRange<T extends Comparable<T>> implements Range<SimpleRange<T>, T> {
	private Bound<T> low;
	private Bound<T> high;
	/**
	 * Creates a new SimpleRange.
	 * 
	 * @param lowerPivot The value of the lower bound.
	 * @param lowerInclusive Whether this range should include the lower bound or not.
	 * @param upperPivot The value of the upper bound.
	 * @param upperInclusive Whether this range should include the upper bound or not.
	 */
	public SimpleRange(T lowerPivot, boolean lowerInclusive, T upperPivot, boolean upperInclusive) {
		this(new Bound<>(lowerPivot, lowerInclusive), new Bound<>(upperPivot, upperInclusive));
	}
	/**
	 * Creates a new SimpleRange
	 * 
	 * @param lowerPivot The value of the lower bound.
	 * @param lowerInclusive Whether this range should include the lower bound or not.
	 * @param upperBound The upper bound of this range.
	 */
	public SimpleRange(T lowerPivot, boolean lowerInclusive, Bound<T> upperBound) {
		this(new Bound<>(lowerPivot, lowerInclusive), upperBound);
	}
	/**
	 * Creates a new SimpleRange
	 * 
	 * @param lowerBound The lower bound of this range.
	 * @param upperPivot The value of the upper bound.
	 * @param upperInclusive Whether this range should include the upper bound or not.
	 */
	public SimpleRange(Bound<T> lowerBound, T upperPivot, boolean upperInclusive) {
		this(lowerBound, new Bound<>(upperPivot, upperInclusive));
	}
	/**
	 * Creates a new SimpleRange
	 * 
	 * @param lowerBound The lower bound of this range.
	 * @param upperBound The upper bound of this range.
	 */
	public SimpleRange(Bound<T> lowerBound, Bound<T> upperBound) {
		Validate.notNull(lowerBound, "lowerBound");
		Validate.notNull(upperBound, "upperBound");
		Range.notSame(lowerBound, upperBound);
		low = lowerBound;
		high = upperBound;
	}
	@Override
	public Bound<T> getLowerBound() {
		return low;
	}
	@Override
	public Bound<T> getUpperBound() {
		return high;
	}
	@Override
	public boolean argInRange(T arg) {
		return !low.argIsBelow(arg) && !high.argIsAbove(arg);
	}
	@Override
	public boolean argNotInRange(T arg) {
		return !argInRange(arg);
	}
	@Override
	public boolean isInside(SimpleRange<T> range) {
		return range.argInRange(low.getPivot()) && range.argInRange(high.getPivot());
	}
	@Override
	public boolean isOutside(SimpleRange<T> range) {
		return !isInside(range);
	}
	@Override
	public boolean overlaps(SimpleRange<T> range) {
		// At least one bound is inside arg range (this is symmetric so no reverse check is required)
		return range.argInRange(low.getPivot()) || range.argInRange(high.getPivot());
	}
	@Override
	public String toString() {
		return Range.toString(this);
	}
}
