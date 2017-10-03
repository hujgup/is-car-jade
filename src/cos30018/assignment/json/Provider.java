package cos30018.assignment.json;

import com.google.gson.Gson;
import jade.imtp.leap.http.HTTPRequest;

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
