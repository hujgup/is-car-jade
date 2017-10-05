package cos30018.assignment.logic;

import java.io.IOException;
import cos30018.assignment.data.SystemData;
import cos30018.assignment.http.Responder;
import cos30018.assignment.utils.Validate;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;

@SuppressWarnings("serial")
public class UpdateServerBehaviour extends ServerBehaviour {
	private SystemData data;
	public UpdateServerBehaviour(SystemData data, int port) throws IOException {
		super(port);
		Validate.notNull(data, "data");
		this.data = data;
	}
	@Override
	protected void handle(IHTTPSession session, Responder responder) {
		// TODO: Real implementation (Trigger timetable re-eval behaviour)
		switch (session.getMethod()) {
			case POST:
				String json = session.getParms().get("json");
				responder.respond("found json:\r\n\r\n" + json);
				break;
			default:
				responder.respond("not a POST request");
				break;
		}
		
	}
}
