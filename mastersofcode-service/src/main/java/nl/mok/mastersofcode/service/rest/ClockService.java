package nl.mok.mastersofcode.service.rest;

import static java.util.logging.Level.INFO;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import nl.mok.mastersofcode.service.core.TimeService;

/**
 * Service to set, start, pause, freeze and stop the clock.
 * 
 * @author Jeroen
 *
 */
@Path("/time")
public class ClockService {

	private final static Logger LOGGER = Logger
			.getLogger("nl.mok.mastersofcode.service");

	@Inject
	private TimeService timeService;

	/**
	 * Pauses the clock.
	 */
	@GET
	@Path("/pause")
	public void pause() {
		if (timeService.isActive()) {
			timeService.pause();
			LOGGER.log(INFO, "Round paused");
		} else {
			LOGGER.log(INFO, "Round resumed");
			timeService.start();
		}
	}

	/**
	 * Freezes the clock.
	 */
	@GET
	@Path("/freeze")
	public void freeze() {
		timeService.freeze();
	}

	/**
	 * Stops the clock. When the clocks stops, the current round will
	 * automatically be stopped.
	 */
	@GET
	@Path("/stop")
	public void stop() {
		LOGGER.log(INFO, "Round manually stopping...");
		timeService.stop();
	}

	/**
	 * Adjusts the clock. The amount of seconds can never be higher than the
	 * total duration of the current round.
	 * 
	 * @param time
	 *            The amount of seconds to set the clock to
	 */
	@GET
	@Path("/set/{time}")
	public void set(@PathParam("time") Integer time) {
		timeService.adjustTime(time);
	}
}