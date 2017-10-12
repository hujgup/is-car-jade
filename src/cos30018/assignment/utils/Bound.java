package cos30018.assignment.utils;

import java.io.Serializable;

/**
 * Represents a bound on a Range.
 * 
 * @author Jake
 *
 * @param <T> The bounding type.
 */
public class Bound<T extends Comparable<T>> implements Serializable {
	private static final long serialVersionUID = -6421980391399733512L;
	private T value;
	private boolean inclusive;
	/**
	 * Creates a new Bound.
	 * 
	 * @param pivot The pivot value of this bound.
	 * @param inclusive Whether this bound should include the pivot value or not.
	 */
	public Bound(T pivot, boolean inclusive) {
		Validate.notNull(pivot, "pivot");
		this.value = pivot;
		this.inclusive = inclusive;
	}
	/**
	 * @return The pivot value.
	 */
	public T getPivot() {
		return value;
	}
	/**
	 * @return Whether or not this bound includes the pivot or not.
	 */
	public boolean isInclusive() {
		return inclusive;
	}
	/**
	 * Checks whether the given argument is below this bound.
	 * 
	 * @param arg The arg to check.
	 * @return True if the given argument is below this bound.
	 */
	public boolean argIsBelow(T arg) {
		int cmp = value.compareTo(arg);
		return inclusive ? cmp < 0 : cmp <= 0;
	}
	/**
	 * Checks whether the given argument is above this bound.
	 * 
	 * @param arg The arg to check.
	 * @return True if the given argument is above this bound.
	 */
	public boolean argIsAbove(T arg) {
		int cmp = value.compareTo(arg);
		return inclusive ? cmp > 0 : cmp >= 0;		
	}
}
