package cos30018.assignment.data;

import java.util.HashMap;
import java.util.LinkedList;
import cos30018.assignment.utils.Validate;
import jade.core.AID;

/**
 * A unique identifier connecting objects and car and scheduling agents.
 * 
 * @author Jake
 */
public class CarID {
	private static HashMap<AID, Integer> ids = new HashMap<>();
	private static LinkedList<AID> schedulers = new LinkedList<>();
	private static HashMap<Integer, CarID> carIds = new HashMap<>();
	private int id;
	private AID carAgent;
	private AID schedulerAgent;
	private CarID(int id, AID carAgent, AID schedulerAgent) {
		Validate.notNull(carAgent, "car");
		Validate.notNull(schedulerAgent, "scheduler");
		this.id = id;
		this.carAgent = carAgent;
		this.schedulerAgent = schedulerAgent;
	}
	/**
	 * Creates a new CarID.
	 * 
	 * @param carAgent The agent ID of the car agent.
	 * @param schedulerAgent The agent ID of the scheduling agent.
	 * @return
	 */
	public static CarID create(AID carAgent, AID schedulerAgent) {
		Validate.notNull(carAgent, "carAgent");
		Validate.notNull(schedulerAgent, "schedulerAgent");
		Validate.keyNotInMap(carAgent, ids, "carAgent");
		Validate.listNotContains(schedulerAgent, schedulers, "schedulerAgent");
		int id = (int)(Math.random()*20000) + 8080;
		ids.put(carAgent, id);
		schedulers.add(schedulerAgent);
		CarID res = new CarID(id, carAgent, schedulerAgent);
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
	/**
	 * @return The ID of the scheduling agent.
	 */
	public AID getScheduler() {
		return schedulerAgent;
	}
	@Override
	public int hashCode() {
		// Generated
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((carAgent == null) ? 0 : carAgent.hashCode());
		result = prime * result + ((schedulerAgent == null) ? 0 : schedulerAgent.hashCode());
		return result;
	}
	public boolean equals(CarID other) {
		return id == other.id;
	}
	@Override
	public boolean equals(Object obj) {
		return obj instanceof CarID ? equals((CarID)obj) : false;
	}
}
