package nl.mok.mastersofcode.service.core;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nl.mok.mastersofcode.service.domain.ClockTick;
import nl.mok.mastersofcode.service.domain.Competition;
import nl.mok.mastersofcode.service.domain.Hint;
import nl.mok.mastersofcode.service.domain.Node;
import nl.mok.mastersofcode.service.domain.Round;
import nl.mok.mastersofcode.service.domain.User;
import nl.mok.mastersofcode.service.messaging.RequestSender;
import nl.mok.mastersofcode.service.rest.NodeService;
import nl.mok.mastersofcode.service.rest.UserService;

/**
 * Competition management service that keeps track of the currently started
 * competitions and rounds. This service manages:
 * <ul>
 * <li>Starting competitions</li>
 * <li>Stopping competitions</li>
 * <li>Starting rounds</li>
 * <li>Broadcasting hints</li>
 * <li>Assigning build nodes to teams</li>
 * </ul>
 * Pausing, freezing and stopping rounds are managed by the time service. A
 * round is automatically stopped when the clock runs out. A round can therefore
 * be stopped by manually stopping the clock.
 * 
 * @author Jeroen Schepens
 */
@Singleton
@Startup
public class CompetitionManager {

	private final static Logger LOGGER = Logger
			.getLogger("nl.mok.mastersofcode.service");

	@PersistenceContext
	private EntityManager em;

	@Inject
	private TimeService timeService;

	@Inject
	private RequestSender requestSender;

	@Inject
	private UserService userService;

	@Inject
	private NodeService nodeService;

	@Inject
	private Router router;

	private Integer currentCompetitionId = null;

	private Integer currentRoundId = null;

	private Set<Hint> hints;

	@PostConstruct
	private void init() {
		//TODO
	}

	/**
	 * Gets the ID of the currently started competition.
	 * 
	 * @return The competition ID
	 */
	public Integer getCurrentCompetition() {
		return 0;
	}

	/**
	 * Sets the ID of the currently started competition. Setting this ID will
	 * effectively start a competition.
	 * 
	 * @param id
	 *            The
	 * @return Boolean indicating if the competition was successfully set.
	 */
	public boolean setCurrentCompetition(Integer id) {
		return true;
	}

	/**
	 * Stops the current competition. This will also stop the current round.
	 */
	public void stopCompetition() {
		//TODO
	}

	/**
	 * Gets the ID of the currently started round
	 * 
	 * @return Current round ID
	 */
	public Integer getCurrentRound() {
		return 0;
	}

	/**
	 * Sets the ID of the current round. This will effectively start a round.
	 * The round must be in the current competition.
	 * 
	 * @param id
	 *            The ID of the round to start
	 * @return True if the round is successfully started, false otherwise
	 */
	public boolean setCurrentRound(Integer id) {
		return true;
	}

	/**
	 * Observer method that is called on every clock tick. When the Clock is
	 * stopped (tick with both remaining and total time zero), the current round
	 * ID will automatically set to null and the round is stopped.
	 * 
	 * @param tick
	 *            The received clock tick
	 */
	public void onTick(@Observes ClockTick tick) {
		//TODO
	}

	/*
	 * TODO Nodes are assigned randomly over all users, not just the users in
	 * the current competition.
	 */
	private void assignNodes() {
		//TODO
	}

	private void sendHint(Hint hint) {
		//TODO
	}
}