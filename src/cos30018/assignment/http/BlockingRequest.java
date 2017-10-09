package cos30018.assignment.http;

import java.util.concurrent.Semaphore;
import cos30018.assignment.utils.Validate;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

/**
 * An HTTP request that blocks the server thread until it is responded to.
 * 
 * @author Jake
 */
public class BlockingRequest {
	private IHTTPSession session;
	private Response response;
	private Semaphore responseSem;
	/**
	 * Creates a new BlockingRequest.
	 * 
	 * @param session The HTTP session this request is wrapping.
	 */
	public BlockingRequest(IHTTPSession session) {
		this.session = session;
		response = null;
		responseSem = new Semaphore(0);
	}
	/**
	 * @return The session this request is wrapping.
	 */
	public IHTTPSession getSession() {
		return session;
	}
	/**
	 * @return True if this request has been responded to.
	 */
	public boolean hasResponse() {
		return response != null;
	}
	/**
	 * Gets the response to this request.
	 * 
	 * @param block Whether or not to block the current thread until the response exists.
	 * @return The response to this request.
	 */
	public Response getResponse(boolean block) {
		if (block) {
			// Semaphore must be at least 1, but after that number of accesses is unbounded
			responseSem.acquireUninterruptibly();
			responseSem.release();
		}
		return getResponse();
	}
	/**
	 * @return The response to this request.
	 */
	public Response getResponse() {
		return response;
	}
	/**
	 * Respond to this request.
	 * 
	 * @param response The response.
	 */
	public void respond(Response response) {
		Validate.notNull(response, "response");
		if (hasResponse()) {
			throw new IllegalStateException("Cannot override response once it has been set.");
		}
		this.response = response;
		responseSem.release();
	}
}
