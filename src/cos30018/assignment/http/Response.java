package cos30018.assignment.http;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import cos30018.assignment.utils.Validate;

/**
 * Represents an HTTP response.
 * 
 * @author Jake
 */
public class Response {
	/**
	 * Builder for an HTTP response.
	 * 
	 * @author Jake
	 */
	public static class Builder {
		private static final HashMap<Integer, String> STATUS_TEXT = new HashMap<>();
		static {
			STATUS_TEXT.put(100, "continue");
			STATUS_TEXT.put(101,"Switching Protocols");
			STATUS_TEXT.put(200,"OK");
			STATUS_TEXT.put(201,"Created");
			STATUS_TEXT.put(202,"Accepted");
			STATUS_TEXT.put(203,"Non-Authoritative Information");
			STATUS_TEXT.put(204,"No Content");
			STATUS_TEXT.put(205,"Reset Content");
			STATUS_TEXT.put(206,"Partial Content");
			STATUS_TEXT.put(300,"Multiple Choices");
			STATUS_TEXT.put(301,"Moved Permanently");
			STATUS_TEXT.put(302,"Found");
			STATUS_TEXT.put(303,"See Other");
			STATUS_TEXT.put(304,"Not Modified");
			STATUS_TEXT.put(305,"Use Proxy");
			STATUS_TEXT.put(307,"Temporary Redirect");
			STATUS_TEXT.put(400,"Bad Request");
			STATUS_TEXT.put(401,"Unauthorized");
			STATUS_TEXT.put(402,"Payment Required");
			STATUS_TEXT.put(403,"Forbidden");
			STATUS_TEXT.put(404,"Not Found");
			STATUS_TEXT.put(405,"Method Not Allowed");
			STATUS_TEXT.put(406,"Not Acceptable");
			STATUS_TEXT.put(407,"Proxy Authentication Required");
			STATUS_TEXT.put(408,"Request Time-out");
			STATUS_TEXT.put(409,"Conflict");
			STATUS_TEXT.put(410,"Gone");
			STATUS_TEXT.put(411,"Length Required");
			STATUS_TEXT.put(412,"Precondition Failed");
			STATUS_TEXT.put(413,"Request Entity Too Large");
			STATUS_TEXT.put(414,"Request-URI Too Large");
			STATUS_TEXT.put(415,"Unsupported Media Type");
			STATUS_TEXT.put(416,"Requested range not satisfiable");
			STATUS_TEXT.put(417,"Expectation Failed");
			STATUS_TEXT.put(500,"Internal Server Error");
			STATUS_TEXT.put(501,"Not Implemented");
			STATUS_TEXT.put(502,"Bad Gateway");
			STATUS_TEXT.put(503,"Service Unavailable");
			STATUS_TEXT.put(504,"Gateway Time-out");
			STATUS_TEXT.put(505,"HTTP Version not supported");
		}
		private String newLine;
		private String bVersion;
		private int bStatus;
		private String bStatusText;
		private HashMap<String, String> bHeaders;
		private StringBuilder bBody;
		/**
		 * Creates a new Request Builder.
		 * 
		 * @param newLine The new line character to use by default when building the response body.
		 */
		public Builder(String newLine) {
			this.newLine = newLine;
			bVersion = Versions.V1_1;
			bStatus = 200;
			bStatusText = "OK";
			bHeaders = new HashMap<>();
			bHeaders.put("Content-Type", "text/plain");
			bBody = new StringBuilder();
		}
		/**
		 * Creates a new Request Builder using CRLF as the default new line character when building the request body.
		 */
		public Builder() {
			this("\r\n");
		}
		private void validateCodeRange(int code) {
			Validate.greaterThanOrEqualTo(code, 100, "code");
			Validate.lessThan(code, 1000, "code");			
		}
		/**
		 * Sets the HTTP version.
		 * 
		 * @param version The HTTP version. Must start with "HTTP/".
		 * @return This object, for chaining.
		 */
		public Builder version(String version) {
			Validate.notNull(version, "version");
			Validate.notEmpty(version, "version");
			Validate.startsWith(version, "HTTP/", "version");
			bVersion = version;
			return this;
		}
		/**
		 * Sets a custom HTTP status code.
		 * By default this is set to 200 OK, so you don't need to manually set that.
		 * 
		 * @param code The status code. Must be in the range [100, 999].
		 * @param text The text describing the status code.
		 * @return This object, for chaining.
		 */
		public Builder customStatus(int code, String text) {
			validateCodeRange(code);
			Validate.notNull(text, "text");
			Validate.notEmpty(text, "text");
			Validate.stringNotContains(text, "text", "\r", "\n");
			bStatus = code;
			bStatusText = text;
			return this;
		}
		/**
		 * Sets the HTTP status code, automatically setting the status text based on RFC2616.
		 * By default this is set to 200 OK, so you don't need to manually set that.
		 * 
		 * @param code The status code. Must be in the range [100, 999] and be one of the status codes defined by RFC2616.
		 * @return This object, for chaining.
		 */
		public Builder status(int code) {
			validateCodeRange(code);
			Validate.keyInMap(code, STATUS_TEXT, "code");
			bStatus = code;
			bStatusText = STATUS_TEXT.get(code);
			return this;
		}
		/**
		 * Sets an HTTP header key/value pair.
		 * By default the Content-Type header is set to text/plain, so you don't need to manually set that.
		 * 
		 * @param key The header key to set. Cannot contain "\r", "\n", or ":".
		 * @param value The header value. Cannot contain "\r", "\n", or ":".
		 * @return This object, for chaining.
		 */
		public Builder header(String key, String value) {
			Validate.notNull(key, "key");
			Validate.notEmpty(key, "key");
			Validate.stringNotContains(key, "key", "\r", "\n", ":");
			Validate.notNull(value, "value");
			Validate.notEmpty(value, "value");
			Validate.stringNotContains(value, "value", "\r", "\n", ":");
			bHeaders.put(key, value);
			return this;
		}
		/**
		 * Appends text to the response body.
		 * 
		 * @param text The text to append.
		 * @return This object, for chaining.
		 */
		public Builder bodyText(String text) {
			Validate.notNull(text, "text");
			bBody.append(text);
			return this;
		}
		/**
		 * Appends a line of text to the response body.
		 * 
		 * @param text The text to append.
		 * @return This object, for chaining.
		 */
		public Builder bodyLine(String text) {
			return bodyText(text).bodyLine();
		}
		/**
		 * Appends a line break to the response body.
		 * 
		 * @return This object, for chaining.
		 */
		public Builder bodyLine() {
			bBody.append(newLine);
			return this;
		}
		/**
		 * @return A Response built from the values in this Builder.
		 */
		public Response build() {
			return new Response(bVersion, bStatus, bStatusText, bHeaders, bBody.toString());
		}
		/**
		 * Builds a Response from this builder and echoes said response to the specified output stream.
		 * 
		 * @param out The output stream to echo to.
		 */
		public void echo(PrintStream out) {
			build().echo(out);
		}
		/**
		 * Builds a Response from this builder and echoes said response to the specified output stream.
		 * 
		 * @param out The output stream to echo to.
		 */
		public void echo(OutputStream out) {
			build().echo(out);
		}
	}
	
	private String version;
	private int status;
	private String statusText;
	private Map<String, String> headers;
	private String body;
	private Response(String version, int status, String statusText, Map<String, String> headers, String body) {
		this.version = version;
		this.status = status;
		this.statusText = statusText;
		this.headers = Collections.unmodifiableMap(headers);
		this.body = body;
	}
	/**
	 * @return The HTTP status code of this response.
	 */
	public int getStatusCode() {
		return status;
	}
	/**
	 * @return The text corresponding to the HTTP status code of this response.
	 */
	public String getStatusText() {
		return statusText;
	}
	/**
	 * Determines whether the specified HTTP header is present.
	 * 
	 * @param key The HTTP header to check for.
	 * @return True if this response has the specified header.
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
	 * @return All the HTTP headers on this response, and their values.
	 */
	public Map<String, String> getAllHeaders() {
		return headers;
	}
	/**
	 * @return The body of this response, which may be in any arbitrary format (the Content-Type header should be set to help the receiver understand how to handle this).
	 */
	public String getBody() {
		return body;
	}
	/**
	 * Echoes this response to the specified output stream.
	 * 
	 * @param out The output stream to echo to.
	 */
	public void echo(PrintStream out) {
		out.print(toString());
	}
	/**
	 * Echoes this response to the specified output stream.
	 * 
	 * @param out The output stream to echo to.
	 */
	public void echo(OutputStream out) {
		echo(new PrintStream(out));
	}
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		String crlf = "\r\n";
		// Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
		b.append(version);
		b.append(" ");
		b.append(status);
		b.append(" ");
		b.append(statusText);
		b.append(crlf);
		for (Entry<String, String> header : headers.entrySet()) {
			b.append(header.getKey());
			b.append(": ");
			b.append(header.getValue());
			b.append(crlf);
		}
		b.append(crlf);
		b.append(body);
		return b.toString();
	}
}
