package cos30018.assignment.logic;

import java.io.IOException;
import cos30018.assignment.ui.http.BlockingRequest;
import cos30018.assignment.ui.http.BlockingServer;
import cos30018.assignment.ui.http.Responder;
import cos30018.assignment.ui.json.Json;
import cos30018.assignment.utils.Validate;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import jade.core.behaviours.CyclicBehaviour;

/**
 * A behaviour that wraps an HTTP server.
 * 
 * @author Jake
 */
@SuppressWarnings("serial")
public abstract class ServerBehaviour extends CyclicBehaviour {
	private static class JsonError {
		private String error;
		public JsonError(String err) {
			error = err;
		}
	}
	
	protected static final String MIME_TYPE = "application/json";
	private BlockingServer server;
	/**
	 * Creates a new ServerBehaviour.
	 * 
	 * @param port The port this server should listen to.
	 * @throws IOException If the server was unable to be created or started.
	 */
	public ServerBehaviour(int port) throws IOException {
		Validate.positive(port, "port");
		server = BlockingServer.create(port);
	}
	/**
	 * Handles a request.
	 * 
	 * @param session The session, containing data pertaining to the request.
	 * @param responder Allows responses to this request.
	 */
	protected abstract void handle(IHTTPSession session, Responder responder);
	protected void generateError(Responder responder, String err) {
		responder.respond(Response.Status.BAD_REQUEST, MIME_TYPE, generateError(err));
	}
	protected String generateError(String err) {
		return Json.serialize(new JsonError(err));
	}
	@Override
	public final void action() {
		BlockingRequest req = server.block();
		handle(req.getSession(), new Responder(req));
		if (!req.hasResponse()) {
			throw new IllegalStateException("Request was not responded to in this object's handle method.");
		}
	}
}
