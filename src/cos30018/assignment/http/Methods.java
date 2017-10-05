package cos30018.assignment.http;

/**
 * Provides consts for HTTP method strings.
 * 
 * @author Jake
 */
public enum Method {
	GET,
	POST,
	HEAD,
	PUT,
	DELETE,
	CONNECT,
	OPTIONS,
	TRACE,
	PATCH
}

public class Methods {
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String HEAD = "HEAD";
	public static final String PUT = "PUT";
	public static final String DELETE = "DELETE";
	public static final String CONNECT = "CONNECT";
	public static final String OPTIONS = "OPTIONS";
	public static final String TRACE = "TRACE";
	public static final String PATCH = "PATCH";
	private Methods() {
	}
}
