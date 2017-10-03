package cos30018.assignment.data;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import cos30018.assignment.utils.LocalTimeRange;
import cos30018.assignment.utils.Validate;

/**
 * Represents a charging timetable.
 * 
 * @author Jake
 */
@SuppressWarnings("serial")
public class Timetable implements Serializable {
	/**
	 * Allows filtering of timetable entries by various constraints.
	 * 
	 * @author Jake
	 */
	public class Filterer {
		private Stream<TimetableEntry> stream;
		private Filterer() {
			stream = entries.stream();
		}
		/**
		 * Filters out any entries that do not correspond to the specified owner.
		 * 
		 * @param owner The owner ID that all remaining entries will have.
		 * @return This object, for chaining.
		 */
		public Filterer byOwner(CarID owner) {
			stream = stream.filter(entry -> entry.getCar().getOwner().equals(owner));
			return this;
		}
		/**
		 * Filters out any entries that do not occur at the specified time.
		 * 
		 * @param time The time that all remaining entries will have inside their time ranges.
		 * @return This object, for chaining.
		 */
		public Filterer atTime(LocalTime time) {
			stream = stream.filter(entry -> entry.getTimeRange().argInRange(time));
			return this;
		}
		/**
		 * Filters out any entries that do not entirely, or at all, sit inside the specified range.
		 * 
		 * @param range The range that all remaining entry ranges will be a subset of.
		 * @return This object, for chaining.
		 */
		public Filterer inTimeRange(LocalTimeRange range) {
			stream = stream.filter(entry -> entry.getTimeRange().isInside(range));
			return this;
		}
		/**
		 * Filters out any entries that do not at least partially lie inside the specified range.
		 * 
		 * @param range The range that all remaining entry ranges will overlap.
		 * @return This object, for chaining.
		 */
		public Filterer overlapsTimeRange(LocalTimeRange range) {
			stream = stream.filter(entry -> entry.getTimeRange().overlaps(range));
			return this;
		}
		/**
		 * @return True if there are no entries that have passed any previously applied filters.
		 */
		public boolean isEmpty() {
			return stream.count() == 0;
		}
		/**
		 * @return The filtered entries as a new list.
		 */
		public List<TimetableEntry> toList() {
			return stream.collect(Collectors.toList());
		}
	}
	
	/**
	 * Represents the result of an add operation to a timetable.
	 * 
	 * @author Jake
	 */
	public static enum AddResult {
		/**
		 * The entry was added successfully.
		 */
		SUCCESS,
		/**
		 * The entry could not be added because it would overlap with another entry for the same car.
		 */
		OVERLAP
	}
	
	private LinkedList<TimetableEntry> entries;
	/**
	 * Creates a new Timetable.
	 * 
	 * @param entries The initial entries to add to this timetable.
	 */
	public Timetable(Iterable<TimetableEntry> entries) {
		this();
		for (TimetableEntry entry : entries) {
			this.entries.add(entry);
		}
	}
	/**
	 * Creates a new Timetable with no entries.
	 */
	public Timetable() {
		entries = new LinkedList<>();
	}
	/**
	 * @return All the entries in this timetable.
	 */
	public List<TimetableEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}
	/**
	 * Attempts to add an entry to this timetable.
	 * 
	 * @param entry The entry to add.
	 * @return An AddResult describing whether the addition was a success, and if it was not, what was wrong with the specified entry that prevented it from being added.
	 */
	public AddResult add(TimetableEntry entry) {
		AddResult res;
		if (entries.isEmpty() || filter().byOwner(entry.getCar().getOwner()).overlapsTimeRange(entry.getTimeRange()).isEmpty()) {
			// Filter is checking if there's another entry for the arg entry's car whose slot overlaps the arg entry's slot
			// (i.e. Any one car cannot be scheduled for charging more than once at any one time)
			entries.add(entry);
			res = AddResult.SUCCESS;
		} else {
			res = AddResult.OVERLAP;
		}
		return res;
	}
	/**
	 * @return A filter object for filtering entries.
	 */
	public Filterer filter() {
		return new Filterer();
	}
}