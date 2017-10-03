package cos30018.assignment.utils;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Provides utility methods for dealing with InputStreams.
 * 
 * @author Jake
 */
public class Input {
	private Input() {
	}
	/**
	 * Converts an entire stream into a string. If the provided stream does not terminate, this method will hang.
	 * 
	 * @param is The input stream to read from.
	 * @return The entire content of the specified input stream, as a string.
	 */
	public static String readAll(InputStream is) {
		// The fact that this method isn't native says a lot about Java
		@SuppressWarnings("resource")
		Scanner s = new Scanner(is);
		s.useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}
