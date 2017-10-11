package cos30018.assignment.ui.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Provides a Gson object for JSON parsing.
 * 
 * @author Jake
 */
public class Provider {
	/**
	 * The Gson object to parse JSON strings with.
	 */
	public static final Gson OBJ = new Gson();
	private Provider() {
	}
}
