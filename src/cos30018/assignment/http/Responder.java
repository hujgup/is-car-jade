package cos30018.assignment.http;

import java.io.InputStream;
import cos30018.assignment.utils.Validate;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.IStatus;

/**
 * Provides a wrapper around response methods. 
 *
 * @author Jake
 */
public class Responder {
	private BlockingRequest req;
	/**
	 * Creates a new Responder.
	 * 
	 * @param req The BlockingRequest this responder creates responses for.
	 */
	public Responder(BlockingRequest req) {
		Validate.notNull(req, "req");
		this.req = req;
	}
	/**
	 * Responds to the wrapped request.
	 * 
	 * @param status The HTTP status code to return.
	 * @param mimeType The MIME type of the response body.
	 * @param data The response body.
	 * @param dataByteLength The length of the response body, in bytes.
	 */
	public void respond(IStatus status, String mimeType, InputStream data, long dataByteLength) {
		Validate.notNull(mimeType, "mimeType");
		Validate.notNull(data, "data");
		Validate.notNegative(dataByteLength, "dataByteLength");
		respond(NanoHTTPD.newFixedLengthResponse(status, mimeType, data, dataByteLength));
	}
	/**
	 * Responds to the wrapped request.
	 * 
	 * @param status The HTTP status code to return.
	 * @param mimeType The MIME type of the response body.
	 * @param data The response body.
	 */
	public void respond(IStatus status, String mimeType, InputStream data) {
		Validate.notNull(mimeType, "mimeType");
		Validate.notNull(data, "data");
		respond(NanoHTTPD.newChunkedResponse(status, mimeType, data));
	}
	/**
	 * Responds to the wrapped request.
	 * 
	 * @param status The HTTP status code to return.
	 * @param mimeType The MIME type of the response body.
	 * @param msg The response body.
	 */
	public void respond(IStatus status, String mimeType, String msg) {
		Validate.notNull(mimeType, "mimeType");
		Validate.notNull(msg, "msg");
		respond(NanoHTTPD.newFixedLengthResponse(status, mimeType, msg));
	}
	/**
	 * Responds to the wrapped request with a status of 200 OK and a MIME type of text/plain.
	 * 
	 * @param msg The response body.
	 */
	public void respond(String msg) {
		Validate.notNull(msg, "msg");
		respond(NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", msg));
	}
	/**
	 * Responds to the wrapped request.
	 * 
	 * @param response The pre-made Response object.
	 */
	public void respond(Response response) {
		Validate.notNull(response, "response");
		response.addHeader("Access-Control-Allow-Origin", "*");
		req.respond(response);
	}
}
