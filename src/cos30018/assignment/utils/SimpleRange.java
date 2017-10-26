package cos30018.assignment.utils;

import java.time.LocalTime;
import java.util.LinkedList;

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
	private static final long serialVersionUID = 5464801255469085737L;
	private static LinkedList<Class<?>> specializations = new LinkedList<>();
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
		for (Class<?> type : specializations) {
			Validate.notClass(lowerBound.getPivot(), LocalTime.class, "<T>");			
		}
		low = lowerBound;
		high = upperBound;
	}
	/**
	 * Registers a range specialization so that users do not create a SimpleRange with that class as the type parameter.
	 * 
	 * Call this method from an object's static {} context.
	 * 
	 * @param type The type that has a specialized implementation.
	 */
	public static void registerSpecialization(Class<?> type) {
		specializations.add(type);
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
		return !isInside(range) && !overlaps(range);
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
