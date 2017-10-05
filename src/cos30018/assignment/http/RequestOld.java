package cos30018.assignment.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP request.
 * 
 * @author Jake
 */
public class RequestOld {
	private String method;
	private String version;
	private HashMap<String, String> headers;
	private HashMap<String, String> args;
	private String body;
	/**
	 * Creates a new Request.
	 * 
	 * @param http The stream containing the HTTP request test.
	 * @throws IOException When the InputStream encounters an error.
	 */
	public RequestOld(InputStream http) throws IOException {
		// Credit to https://stackoverflow.com/a/21620603
		headers = new HashMap<>();
		args = new HashMap<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(http));
		StringBuilder b = new StringBuilder();
		boolean inBody = false;
		boolean isFirstBodyLine = true;
		String[] split;
		String headerKey;
		String headerValue;
		String line;
		int index;
		while ((line = br.readLine()) != null) {
			if (inBody) {
				if (isFirstBodyLine) {
					isFirstBodyLine = false;
				} else {
					b.append('\n');
				}
				b.append(line);
			}
			if (line.isEmpty()) {
				inBody = true;
			} else {
				index = line.indexOf(':');
				if (index > -1) {
					headerKey = line.substring(0, index).trim();
					headerValue = line.substring(index + 1).trim();
					headers.put(headerKey, headerValue);
				} else {
					split = line.split("\\/", 2);
					method = split[0].trim();
					version = split[1].trim();
				}
			}
		}
		body = b.toString();
	}
	/**
	 * @return The HTTP method.
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @return The HTTP protocol version.
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * Determines whether the specified HTTP header is present.
	 * 
	 * @param key The HTTP header to check for.
	 * @return True if this request has the specified header.
	 */
	public boolean hasHeader(String key) {
		return headers.containsKey(key);
	}
	/**
	 * Gets the value corresponding to the specified HTTP header.
	 * 
	 * @param key The HTTP header to get the value of.
	 * @return The value of the specified HTTP header.
	 */
	public String getHeader(String key) {
		return headers.get(key);
	}
	/**
	 * @return All the HTTP headers on this request, and their values.
	 */
	public Map<String, String> getAllHeaders() {
		return Collections.unmodifiableMap(headers);
	}
	/**
	 * @return The body of this request, which may be in any arbitrary format (the Content-Type header may provide a hint as to what to do with this).
	 */
	public String getBody() {
		return body;
	}
}
