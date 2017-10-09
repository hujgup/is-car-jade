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
import cos30018.assignment.http.Responder;
import cos30018.assignment.json.JsonData;
import cos30018.assignment.json.LocalTimeRangeJson;
import cos30018.assignment.json.Provider;
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
	private static class Updater<TIn, TOut> {
		private Method getter;
		private Method setter;
		private Function<TIn, TOut> translator;
		public Updater(String getterName, String setterName, Class<?> setterClass, Function<TIn, TOut> translator) throws NoSuchMethodException, SecurityException {
			getter = JsonData.class.getMethod(getterName);
			setter = Car.class.getMethod(setterName, setterClass);
			this.translator = translator;
		}
		@SuppressWarnings("unchecked")
		public Updater(String getterName, String setterName, Class<?> setterClass) throws NoSuchMethodException, SecurityException {
			this(getterName, setterName, setterClass, x -> (TOut)x);
		}
		@SuppressWarnings("unchecked")
		public void update(JsonData dataInstance, Object setterInstance) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			TIn value = (TIn)getter.invoke(dataInstance);
			if (value != null) {
				setter.invoke(setterInstance, translator.apply(value));
			}
		}
	}
	
	private static final Updater<Double, Double> U_MGLOAD;
	private static final Updater<Double, Double> U_CCUR;
	private static final Updater<Double, Double> U_CCAP;
	private static final Updater<Double, Double> U_CRATE;
	private static final Updater<List<LocalTimeRangeJson>, List<LocalTimeRange>> U_UATIMES;
	static {
		try {
			U_MGLOAD = new Updater<>("getMaxGridLoad", "setMaxGridLoad", double.class);
			U_CCUR = new Updater<>("getCurrentCharge", "setCurrentCharge", double.class);
			U_CCAP = new Updater<>("getChargeCapacity", "setChargeCapacity", double.class);
			U_CRATE = new Updater<>("getChargeRate", "setChargeRate", double.class);
			U_UATIMES = new Updater<>("getUnavailableTimes", "setUnavailableTimes", List.class, jsonList -> jsonList.stream().map(jsonRange -> {
				return new LocalTimeRange(
					LocalTime.parse(jsonRange.getLowerBound().getPivot()),
					jsonRange.getLowerBound().getInclusive(),
					LocalTime.parse(jsonRange.getUpperBound().getPivot()),
					jsonRange.getUpperBound().getInclusive()
				);
			}).collect(Collectors.toList()));
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	private Environment data;
	private CarID id;
	/**
	 * Creates a new UpdateServerBehaviour.
	 * 
	 * @param data The environment data the agent this behaviour corresponds to knows about.
	 * @param port The port the server should listen to.
	 * @throws IOException If the server was unable to be created or started.
	 */
	public UpdateServerBehaviour(Environment data, int port) throws IOException {
		super(port);
		Validate.notNull(data, "data");
		this.data = data;
		id = CarID.fromID(port);
	}
	private void tryUpdate(Updater<?, ?> updater, JsonData dataInstance, Object setterInstance) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		updater.update(dataInstance, setterInstance);
	}
	private void updateConstraints(JsonData jsonData, boolean isNewCar) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		tryUpdate(U_MGLOAD, jsonData, data);
		if (jsonData.isCarConstraintUpdate()) {
			Car car = isNewCar ? new Car(id, 0, Double.MIN_VALUE, Double.MIN_VALUE) : data.getCar(id);
			if (isNewCar) {
				data.addCar(car);
			}
			tryUpdate(U_CCUR, jsonData, car);
			tryUpdate(U_CCAP, jsonData, car);
			tryUpdate(U_CRATE, jsonData, car);
			tryUpdate(U_UATIMES, jsonData, car);
		}
	}
	private void initiateNegotiation() {
		// TODO: Initiate negotiation process
	}
	@Override
	protected void handle(IHTTPSession session, Responder responder) {
		switch (session.getMethod()) {
			case POST:
				if (session.getParms().containsKey("json")) {
					try {
						JsonData jsonData = JsonData.fromJson(session.getParms().get("json"));
						boolean isNewCar = data.hasCar(id);
						jsonData.validate(isNewCar);
						if (jsonData.isConstraintUpdate()) {
							updateConstraints(jsonData, isNewCar);
							// TODO: Always update other agents if maxGridLoad constraint changed (tell them what it changed to)
							// TODO: Detect if timetable change is required, and do changes, updating or negotiating as needed.
							responder.respond("Constraints for car " + id.getID() + " updated to:\r\n\r\n" + Provider.OBJ.toJson(data.toJson(id)));
						} else {
							initiateNegotiation();
						}
					} catch (Throwable e) {
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
