package cos30018.assignment.ui.json;

import java.util.List;

public class TimetableEntryJson {
	@SuppressWarnings("unused")
	private int id;
	@SuppressWarnings("unused")
	private List<Integer> slots;
	public TimetableEntryJson(int id, List<Integer> slots) {
		this.id = id;
		this.slots = slots;
	}
}
