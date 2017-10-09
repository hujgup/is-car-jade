package cos30018.assignment.logic;

import java.io.IOException;
import cos30018.assignment.http.BlockingRequest;
import cos30018.assignment.http.BlockingServer;
import cos30018.assignment.http.Responder;
import cos30018.assignment.utils.Validate;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import jade.core.behaviours.CyclicBehaviour;

/**
 * A behaviour that wraps an HTTP server.
 * 
 * @author Jake
 */
@SuppressWarnings("serial")
public abstract class ServerBehaviour extends CyclicBehaviour {
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
	@Override
	public final void action() {
		BlockingRequest req = server.block();
		handle(req.getSession(), new Responder(req));
		if (!req.hasResponse()) {
			throw new IllegalStateException("Request was not responded to in this object's handle method.");
		}
	}
}
