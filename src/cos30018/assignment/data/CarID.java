package cos30018.assignment.data;

import cos30018.assignment.utils.Validate;
import jade.core.AID;

/**
 * A unique identifier connecting objects and car and scheduling agents.
 * 
 * @author Jake
 */
public class CarID {
	// TODO: Consider if CarID should be agent ID independent.
	private AID car;
	private AID scheduler;
	/**
	 * Creates a new CarID.
	 * 
	 * @param car The car agent's ID.
	 * @param scheduler The scheduling agent's ID.
	 */
	public CarID(AID car, AID scheduler) {
		Validate.notNull(car, "car");
		Validate.notNull(scheduler, "scheduler");
		this.car = car;
		this.scheduler = scheduler;
	}
	/**
	 * @return The ID of the car agent.
	 */
	public AID getCar() {
		return car;
	}
	/**
	 * @return The ID of the scheduling agent.
	 */
	public AID getScheduler() {
		return scheduler;
	}
	@Override
	public int hashCode() {
		// Generated
		final int prime = 31;
		int result = 1;
		result = prime * result + ((car == null) ? 0 : car.hashCode());
		result = prime * result + ((scheduler == null) ? 0 : scheduler.hashCode());
		return result;
	}
	public boolean equals(CarID other) {
		return car.equals(other.car) && scheduler.equals(other.scheduler);
	}
	@Override
	public boolean equals(Object obj) {
		return obj instanceof CarID ? equals((CarID)obj) : false;
	}
}
