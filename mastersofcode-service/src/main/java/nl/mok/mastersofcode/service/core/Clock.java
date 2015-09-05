package nl.mok.mastersofcode.service.core;

import nl.mok.mastersofcode.service.domain.ClockTick;

/**
 * Represents the Clock. When the tick methods is called, it will generate a
 * ClockTick object which respresents the current state of the Clock.
 * 
 * @author Jeroen Schepens
 */
public class Clock {

	private int remaining;
	private int total;

	/**
	 * Ticks the clock. When the clock ticks, the amount of remaining seconds is
	 * decremented and the current state is returned.
	 * 
	 * @return The state of the clock after the tick
	 */
	public ClockTick tick() {
		if (remaining > 0) {
			remaining--;
			return new ClockTick(remaining, total);
		} else {
			throw new IllegalStateException("NO TIKZ MADDAFAKKA");
		}
	}

	/**
	 * Gets the current state of the Clock.
	 * 
	 * @return The current state of the Clock
	 */
	public ClockTick getTime() {
		return new ClockTick(remaining, total);
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
	 * Sets the amount of remaining seconds.
	 * 
	 * @param remaining
	 *            The amount of remaining seconds
	 */
	public void setRemaining(int remaining) {
		this.remaining = remaining;
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
	 * Sets the total amount of seconds.
	 * 
	 * @param total
	 *            The total amount of seconds
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * Checks if the Clock is active. The Clock is active when 1 or more seconds
	 * are remaining.
	 * 
	 * @return Boolean indicating if the clock is active
	 */
	public boolean isActive() {
		return remaining > 0;
	}

	@Override
	public String toString() {
		return "Clock [remaining=" + remaining + ", total=" + total + "]";
	}
}