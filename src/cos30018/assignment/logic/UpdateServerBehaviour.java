package cos30018.assignment.logic;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import cos30018.assignment.data.Car;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;
import cos30018.assignment.ui.http.Responder;
import cos30018.assignment.ui.json.JsonData;
import cos30018.assignment.ui.json.LocalTimeRangeJson;
import cos30018.assignment.ui.json.Provider;
import cos30018.assignment.utils.LocalTimeRange;
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
	/**
	 * Creates a new UpdateServerBehaviour.
	 * 
	 * @param data The environment data the agent this behaviour corresponds to knows about.
	 * @param port The port the server should listen to.
	 * @throws IOException If the server was unable to be created or started.
	 */
	public UpdateServerBehaviour(Environment data, CarID id) throws IOException {
		super(id.getID());
		Validate.notNull(data, "data");
		Validate.notNull(id, "id");
		System.out.println("Port number: " + id.getID());
		this.data = data;
		this.id = id;
	}
	private void initiateNegotiation() {
		// TODO: Initiate negotiation process
	}
	/*
JS test code:

var req = new XMLHttpRequest();
req.open("POST", "http://localhost:[PORT NUMBER]/", true);
req.onreadystatechange = function() {
	if (req.readyState === 4) {
		console.log(req.responseText);
	}
};
req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
req.send("json=" + JSON.stringify({
	"action": "constraints",
	"maxGridLoad": 80.32,
	"currentCharge": 0,
	"chargeCapacity": 100,
	"chargePerHour": 34,
	"unavailableTimes": [
		{
			"lowerBound": {
				"pivot": "12:34:56",
				"inclusive": false
			},
			"upperBound": {
				"pivot": "17:12",
				"inclusive": true
			}
		}
	]
}));
	*/
	@Override
	protected void handle(IHTTPSession session, Responder responder) {
		switch (session.getMethod()) {
			case POST:
				if (session.getParms().containsKey("json")) {
					try {
						JsonData jsonData = JsonData.fromJson(session.getParms().get("json"));
						jsonData.validate(data, id);
						if (jsonData.isConstraintUpdate()) {
							jsonData.updateEnvironment(data, id);
							// TODO: Always update other agents if maxGridLoad constraint changed (tell them what it changed to)
							// TODO: Detect if timetable change is required, and do changes, updating or negotiating as needed.
							responder.respond(Provider.OBJ.toJson(data.toJson(id)));
						} else {
							initiateNegotiation();
							responder.respond("{}");
						}
					} catch (Throwable e) {
						e.printStackTrace();
						responder.respond(Response.Status.BAD_REQUEST, "text/plain", "ERR: Key \"json\" contained malformed JSON.\r\n" + e.getMessage());
					}
				} else {
					responder.respond(Response.Status.BAD_REQUEST, "text/plain", "ERR: Key \"json\" was not present.");
				}
				break;
			default:
				responder.respond(Response.Status.BAD_REQUEST, "text/plain", "ERR: Not a POST request.");
				break;
		}
		
	}
}
