package cos30018.assignment.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import cos30018.assignment.utils.Validate;
import jade.core.AID;

/**
 * A unique identifier connecting objects and car and scheduling agents.
 * 
 * @author Jake
 */
public class CarID implements Serializable {
	private static final long serialVersionUID = -2949760205672878537L;
	private static HashMap<Integer, AID> ids = new HashMap<>();
	private int id;
	private transient AID carAgent;
	private CarID(int id, AID carAgent) {
		this.id = id;
		this.carAgent = carAgent;
	}
	public static int generateID() {
		int id;
		do {
			// Random, but no duplicates
			id = (int)(Math.random()*20000) + 8081;			
		} while (ids.containsKey(id));
		ids.put(id, null);
		return id;
	}
	/**
	 * Creates a new CarID.
	 * 
	 * @param carAgent The agent ID of the car agent.
	 * @return
	 */
	public static CarID create(int id, AID carAgent) {
		Validate.inSet(id, ids.keySet(), "id", "ids");
		Validate.notNull(carAgent, "carAgent");
		Validate.keyNotInMap(carAgent, ids, "carAgent");
		ids.put(id, carAgent);
		return new CarID(id, carAgent);
	}
	/**
	 * Gets an existing CarID from its integral ID value.
	 * 
	 * @param id The integral ID value.
	 * @return The CarID corresponding to id, or null if none exists.
	 */
	public static CarID fromID(int id) {
		return new CarID(id, ids.get(id));
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
