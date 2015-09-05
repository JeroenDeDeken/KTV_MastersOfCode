package nl.mok.mastersofcode.service.domain;

/**
 * Represents a state of a Clock.
 * 
 * @author Jeroen Schepens
 */
public class ClockTick {

	private final int remaining;
	private final int total;

	public ClockTick(int remaining, int total) {
		this.remaining = remaining;
		this.total = total;
	}

	/**
	 * Gets the amount of remaining seconds.
	 * 
	 * @return The amount of remaining seconds
	 */
	public int getRemaining() {
		return remaining;
	}

	/**
	 * Gets the total amount of seconds.
	 * 
	 * @return The total amount of seconds
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Checks if the state is active. The state is active when both the
	 * remaining time and total time are not zero.
	 * 
	 * @return Boolean indicating if the clock is active
	 */
	public boolean isActive() {
		return !(remaining == 0 && total == 0);
	}

	@Override
	public String toString() {
		return "ClockTick [remaining=" + remaining + ", total=" + total + "]";
	}
}