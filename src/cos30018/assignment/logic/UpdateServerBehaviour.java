package cos30018.assignment.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Function;

import cos30018.assignment.data.Car;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;
import cos30018.assignment.data.Timetable;
import cos30018.assignment.ui.http.Responder;
import cos30018.assignment.ui.json.Json;
import cos30018.assignment.ui.json.JsonData;
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
	private static class JsonError {
		private String error;
		public JsonError(String err) {
			error = err;
		}
	}
	
	private static final String MIME_TYPE = "application/json";
	private Environment data;
	private CarID id;
	private Function<Boolean, ActionResult<Timetable>> callback;
	/**
	 * Creates a new UpdateServerBehaviour.
	 * 
	 * @param data The environment data the agent this behaviour corresponds to knows about.
	 * @param port The port the server should listen to.
	 * @throws IOException If the server was unable to be created or started.
	 */
	// ActionResult<Timetable> negotiateTimetable(boolean constraintsWereUpdated) 
	public UpdateServerBehaviour(Environment data, CarID id, Function<Boolean, ActionResult<Timetable>> messageSender) throws IOException {
		super(id.getID());
		Validate.notNull(data, "data");
		Validate.notNull(id, "id");
		System.out.println("Port number: " + id.getID());
		System.out.println("The car ID is: " + id);
		this.data = data;
		this.id = id;
		this.callback = messageSender;
	}
	private void generateError(Responder responder, String err) {
		responder.respond(Response.Status.BAD_REQUEST, MIME_TYPE, generateError(err));
	}
	private String generateError(String err) {
		return Json.serialize(new JsonError(err));
	}
	public ActionResult<Timetable> negotiateTimetable(boolean constraintsWereUpdated) {
		return callback.apply(constraintsWereUpdated);
	}
	@Override
	protected void handle(IHTTPSession session, Responder responder) {
		switch (session.getMethod()) {
			case POST:
				if (session.getParms().containsKey("json")) {
					try {
						JsonData jsonData = JsonData.fromJson(session.getParms().get("json"));
						jsonData.validate(data, id);
						try {
							if (jsonData.isConstraintUpdate()) {
								jsonData.updateEnvironment(data, id);
							}
							ActionResult<Timetable> res = negotiateTimetable(jsonData.isConstraintUpdate());
							Validate.notNull(res, "res");
							String resJson = Json.serialize(res);
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
