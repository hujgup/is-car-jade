package cos30018.assignment.logic;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;
import cos30018.assignment.ui.http.Responder;
import cos30018.assignment.ui.json.Action;
import cos30018.assignment.ui.json.Json;
import cos30018.assignment.ui.json.JsonData;
import cos30018.assignment.ui.json.TimetableEntryJson;
import cos30018.assignment.utils.Validate;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

/**
 * A behaviour that wraps an HTTP server, reading JSON data from the UI layer.
 * 
 * @author Jake
 */
@SuppressWarnings("serial")
public class UpdateServerBehaviour extends ServerBehaviour {
	private Environment data;
	private CarID id;
	private Function<Boolean, ActionResult<List<TimetableEntryJson>>> callback;
	/**
	 * Creates a new UpdateServerBehaviour.
	 * 
	 * @param data The environment data the agent this behaviour corresponds to knows about.
	 * @param port The port the server should listen to.
	 * @throws IOException If the server was unable to be created or started.
	 */
	public UpdateServerBehaviour(Environment data, CarID id, Function<Boolean, ActionResult<List<TimetableEntryJson>>> messageSender) throws IOException {
		super(id.getID());
		Validate.notNull(data, "data");
		Validate.notNull(id, "id");
		this.data = data;
		this.id = id;
		this.callback = messageSender;
	}
	public ActionResult<List<TimetableEntryJson>> negotiateTimetable(boolean constraintsWereUpdated) {
		return callback.apply(constraintsWereUpdated);
	}
	@Override
	protected void handle(IHTTPSession session, Responder responder) {
		switch (session.getMethod()) {
			case POST:
				if (session.getParms().containsKey("json")) {
					try {
						JsonData jsonData = JsonData.fromJson(session.getParms().get("json"));
						jsonData.validate(data, id, Action.UPDATE_CONSTRAINTS, Action.FORCE_NEGOTIATE);
						try {
							if (jsonData.isConstraintUpdate()) {
								jsonData.updateEnvironment(data, id);
							}
							ActionResult<List<TimetableEntryJson>> res = negotiateTimetable(jsonData.isConstraintUpdate());
							Validate.notNull(res, "res");
							String resJson = Json.serialize(res.toJson());
							if (res.hasResult()) {
								System.out.println("Success.");
								responder.respond(MIME_TYPE, resJson);
							} else {
								System.out.println("ERR: Error from negotiation.");
								responder.respond(Response.Status.INTERNAL_ERROR, MIME_TYPE, resJson);
							}
						} catch (IllegalArgumentException e) {
							System.out.println("ERR: Validation failed.");
							e.printStackTrace();
							responder.respond(Response.Status.INTERNAL_ERROR, MIME_TYPE, generateError("Validation failed: " + e.getMessage()));														
						} catch (Throwable e) {
							System.out.println("ERR: Exception.");
							e.printStackTrace();
							responder.respond(Response.Status.INTERNAL_ERROR, MIME_TYPE, generateError("Unhandled exception: " + e.getMessage()));							
						}
					} catch (Throwable e) {
						System.out.println("ERR: Data was not JSON.");
						e.printStackTrace();
						generateError(responder, "Key \"json\" contained malformed JSON: " + e.getMessage());
					}
				} else {
					System.out.println("ERR: No JSON key.");
					generateError(responder, "Key \"json\" was not present.");
				}
				break;
			default:
				System.out.println("ERR: Wrong method.");
				generateError(responder, "Not a POST request.");
				break;
		}
		
	}
}
