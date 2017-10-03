package cos30018.assignment.data;

import java.util.List;
import cos30018.assignment.utils.LocalTimeRange;

public interface ImmutableCar {
	/**
	 * @return The IDs of the agents that own this car.
	 */
	CarID getOwner();
	/**
	 * @return The current charge in this car.
	 */
	double getCurrentCharge();
	/**
	 * @return The maximum amount of charge this car can hold.
	 */
	double getChargeCapacity();
	/**
	 * @return The amount of charge this car will gain per hour it spends charging.
	 */
	double getChargePerHour();
	/**
	 * @return The times when this car will not be able to be charged (i.e. is in use).
	 */
	List<LocalTimeRange> getUnavailableTimes();
	/**
	 * @return The intrinsic negotiation order.
	 */
	int getNegotiationOrder();
}
