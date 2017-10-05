package cos30018.assignment.http;

import java.io.InputStream;
import cos30018.assignment.utils.Validate;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.IStatus;

public class Responder {
	private BlockingRequest req;
	public Responder(BlockingRequest req) {
		Validate.notNull(req, "req");
		this.req = req;
	}
	public void respond(IStatus status, String mimeType, InputStream data, long dataByteLength) {
		Validate.notNull(mimeType, "mimeType");
		Validate.notNull(data, "data");
		Validate.notNegative(dataByteLength, "dataByteLength");
		respond(NanoHTTPD.newFixedLengthResponse(status, mimeType, data, dataByteLength));
	}
	public void respond(IStatus status, String mimeType, InputStream data) {
		Validate.notNull(mimeType, "mimeType");
		Validate.notNull(data, "data");
		respond(NanoHTTPD.newChunkedResponse(status, mimeType, data));
	}
	public void respond(IStatus status, String mimeType, String msg) {
		Validate.notNull(mimeType, "mimeType");
		Validate.notNull(msg, "msg");
		respond(NanoHTTPD.newFixedLengthResponse(status, mimeType, msg));
	}
	public void respond(String msg) {
		Validate.notNull(msg, "msg");
		respond(NanoHTTPD.newFixedLengthResponse(msg));
	}
	public void respond(Response response) {
		Validate.notNull(response, "response");
		req.respond(response);
	}
}
