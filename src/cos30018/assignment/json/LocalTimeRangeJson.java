package cos30018.assignment.json;

/**
 * Represents a LocalTimeRange in a JSON-convertible form.
 * 
 * @author Jake
 */
public class LocalTimeRangeJson {
	private LocalTimeBoundJson lowerBound;
	private LocalTimeBoundJson upperBound;
	/**
	 * Creates a new LocalTimeRangeJson.
	 * 
	 * @param low The lower bound.
	 * @param high The upper bound.
	 */
	public LocalTimeRangeJson(LocalTimeBoundJson low, LocalTimeBoundJson high) {
		lowerBound = low;
		upperBound = high;
	}
	private LocalTimeRangeJson() {
	}
	/**
	 * @return The lower bound of this range.
	 */
	public LocalTimeBoundJson getLowerBound() {
		return lowerBound;
	}
	/**
	 * @return The upper bound of this range.
	 */
	public LocalTimeBoundJson getUpperBound() {
		return upperBound;
	}
}
