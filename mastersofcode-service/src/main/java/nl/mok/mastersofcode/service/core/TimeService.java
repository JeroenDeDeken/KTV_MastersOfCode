package nl.mok.mastersofcode.service.core;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import nl.mok.mastersofcode.service.domain.ClockTick;

/**
 * Time service that manages the clock. It handles setting, starting, stopping
 * and pausing the clock. The service also sends CDI events to the websocket
 * clients listening to the TimeEndpoint.
 * <p>
 * The Manager listens to the clock and will automatically stop a round when the
 * clock reaches zero.
 * </p>
 * 
 * @author Jeroen Schepens
 */
@Singleton
@Startup
public class TimeService {

	private Clock clock = new Clock();
	private boolean active = false;
	private boolean frozen = false;
	// private short ticks = 0;

	@Inject
	private Event<ClockTick> clockTickEvent;

	/**
	 * Clock tick method that is called every second. It will fire a CDI event
	 * with a tick object.
	 */
	@Schedule(second = "*/1", minute = "*", hour = "*", persistent = false)
	private void tick() {
		if (!clock.isActive()) {
			stop();
		}
		if (active) {
			clockTickEvent.fire(clock.tick());
			return;
		} else {
			clockTickEvent.fire(clock.getTime());
		}
	}

	/**
	 * Gets the current remaining time in seconds.
	 * 
	 * @return The remaingin time
	 */
	public int getRemainingTime() {
		return clock.getRemaining();
	}

	/**
	 * Sets the clock to a given amount of seconds. The clock counts down from
	 * this amount. The clock has to be set before it can be started.
	 * 
	 * @param seconds
	 *            The amount of seconds to set
	 */
	public void setClock(int seconds) {
		pause();
		clock.setTotal(seconds);
		clock.setRemaining(seconds);
	}

	/**
	 * Sets the remaining amount of seconds to a given number. This number is
	 * capped to the total amount of seconds.
	 * 
	 * @param seconds
	 *            The amount of seconds to set the clock to
	 */
	public void adjustTime(int seconds) {
		if (seconds < clock.getTotal()) {
			clock.setRemaining(seconds);
		} else {
			clock.setRemaining(clock.getTotal());
		}
	}

	/**
	 * Starts the clock. If will start if the clock was set, but not started
	 * yet. It will resume if the clock was paused. When the clock was frozen,
	 * it will also be unfrozen.
	 */
	public void start() {
		this.active = true;
		this.frozen = false;
	}

	/**
	 * Pauses the clock.
	 */
	public void pause() {
		this.active = false;
	}

	/**
	 * Freezes the clock. The frozen state is the same as the paused state,
	 * except that all EditorRequests except GET will be blocked.
	 */
	public void freeze() {
		this.active = false;
		this.frozen = true;
	}

	/**
	 * Stops the clock. If the clock is stopped, it has to be set again before
	 * it can be started again.
	 */
	public void stop() {
		setClock(0);
	}

	/**
	 * Returns a boolean indicating if the clock is running.
	 * 
	 * @return Boolean indicating if the clock is running
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Returns a boolean indicating if the clock is frozen.
	 * 
	 * @return Boolean indicating if the clock is frozen
	 */
	public boolean isFrozen() {
		return frozen;
	}
}