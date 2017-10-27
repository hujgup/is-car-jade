package cos30018.assignment.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import cos30018.assignment.ui.json.JsonConvertible;
import cos30018.assignment.utils.Validate;
import jade.core.AID;

/**
 * A unique identifier connecting objects and car and scheduling agents.
 * 
 * @author Jake
 */
public class CarID implements JsonConvertible<Integer>, Serializable {
	private static final long serialVersionUID = -2949760205672878537L;
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(id);
		out.writeObject(carAgent);
	}
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		id = in.readInt();
		carAgent = (AID)in.readObject();
		if (!ids.containsKey(carAgent)) {
			ids.put(carAgent, id);
		}
		if (!carIds.containsKey(id)) {
			carIds.put(id, this);
		}
	}
	private static HashMap<AID, Integer> ids = new HashMap<>();
	private static HashMap<Integer, CarID> carIds = new HashMap<>();
	private int id;
	private transient AID carAgent;
	private CarID(int id, AID carAgent) {
		Validate.notNull(carAgent, "car");
		this.id = id;
		this.carAgent = carAgent;
	}
	/**
	 * Creates a new CarID.
	 * 
	 * @param carAgent The agent ID of the car agent.
	 * @return
	 */
	public static CarID create(AID carAgent) {
		Validate.notNull(carAgent, "carAgent");
		Validate.keyNotInMap(carAgent, ids, "carAgent");
		int id;
		do {
			// Random, but no duplicates
			id = (int)(Math.random()*20000) + 8080;			
		} while (carIds.containsKey(id));
		ids.put(carAgent, id);
		CarID res = new CarID(id, carAgent);
		carIds.put(id, res);
		return res;
	}
	/**
	 * Gets an existing CarID from its integral ID value.
	 * 
	 * @param id The integral ID value.
	 * @return The CarID corresponding to id, or null if none exists.
	 */
	public static CarID fromID(int id) {
		return carIds.get(id);
	}
	/**
	 * @return The unique ID of a car/scheduler pair.
	 */
	public int getID() {
		return id;
	}
	/**
	 * @return The ID of the car agent.
	 */
	public AID getCar() {
		return carAgent;
	}
	public Integer toJson() {
		return id;
	}
	@Override
	public int hashCode() {
		return id;
	}
	public boolean equals(CarID other) {
		return id == other.id;
	}
	@Override
	public boolean equals(Object obj) {
		return obj instanceof CarID ? equals((CarID)obj) : false;
	}
}
