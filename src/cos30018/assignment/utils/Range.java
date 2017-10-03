package cos30018.assignment.utils;

/**
 * Represents a range of values.
 * 
 * @author Jake
 *
 * @param <TSelf> The type that is implementing this interface.
 * @param <T> The bounding type.
 */
public interface Range<TSelf extends Range<TSelf, T>, T extends Comparable<T>> {
	/**
	 * @return The lower bound of this range.
	 */
	Bound<T> getLowerBound();
	/**
	 * @return The upper bound of this range.
	 */
	Bound<T> getUpperBound();
	/**
	 * Checks whether the given argument is inside this range.
	 * 
	 * @param arg The arg to check.
	 * @return True if the given argument is inside this range.
	 */
	boolean argInRange(T arg);
	/**
	 * Checks whether the given argument is outside this range.
	 * 
	 * @param arg The arg to check.
	 * @return True if the given argument is outside this range.
	 */
	boolean argNotInRange(T arg);
	/**
	 * Checks whether this range is a strict subset of the argument range.
	 * 
	 * @param range The range that might be a superset of this range.
	 * @return True if this range is a subset of the provided range.
	 */
	boolean isInside(TSelf range);
	/**
	 * Checks whether this range is not a strict subset of the argument range.
	 * 
	 * @param range The range that might be a superset of this range.
	 * @return True if this range is not a subset of the provided range.
	 */
	boolean isOutside(TSelf range);
	/**
	 * Checks whether this range and another range overlap each other (i.e. whether there exists at least one value that is shared between both ranges).
	 * 
	 * @param range The range that might overlap this range
	 * @return True if this range and the specified range overlap.
	 */
	boolean overlaps(TSelf range);
	/**
	 * Converts any range into a standard string form.
	 * 
	 * @param range The range to convert.
	 * @return The string value of the specified range.
	 */
	static String toString(Range<?, ?> range) {
		String res = range.getLowerBound().isInclusive() ? "[" : "(";
		res += range.getLowerBound().getPivot().toString();
		res += ", ";
		res += range.getUpperBound().isInclusive() ? "]" : ")";
		return res;
	}
	/**
	 * Validates that two bounds are not the same.
	 * 
	 * @param lowerBound The lower bound.
	 * @param upperBound The upper bound.
	 */
	static <T2 extends Comparable<T2>> void notSame(Bound<T2> lowerBound, Bound<T2> upperBound) {
		if (lowerBound.isInclusive() ^ upperBound.isInclusive()) {
			Validate.lessThan(lowerBound.getPivot(), upperBound.getPivot(), "lowerBound pivot", "upperBound pivot");
		} else {			
			Validate.lessThanOrEqualTo(lowerBound.getPivot(), upperBound.getPivot(), "lowerBound pivot", "upperBound pivot");
		}
	}
}