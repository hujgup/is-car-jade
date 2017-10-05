package cos30018.assignment.http;

import java.util.concurrent.Semaphore;
import cos30018.assignment.utils.Validate;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

public class BlockingRequest {
	private IHTTPSession session;
	private Response response;
	private Semaphore responseSem;
	public BlockingRequest(IHTTPSession session) {
		this.session = session;
		response = null;
		responseSem = new Semaphore(0);
	}
	public IHTTPSession getSession() {
		return session;
	}
	public boolean hasResponse() {
		return response != null;
	}
	public Response getResponse(boolean block) {
		if (block) {
			// Semaphore must be at least 1, but after that number of accesses is unbounded
			responseSem.acquireUninterruptibly();
			responseSem.release();
		}
		return getResponse();
	}
	public Response getResponse() {
		return response;
	}
	public void respond(Response response) {
		Validate.notNull(response, "response");
		if (hasResponse()) {
			throw new IllegalStateException("Cannot override response once it has been set.");
		}
		this.response = response;
		responseSem.release();
	}
}
