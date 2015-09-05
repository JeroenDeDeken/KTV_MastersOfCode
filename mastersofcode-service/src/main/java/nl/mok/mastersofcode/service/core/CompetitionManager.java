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
		LOGGER.log(INFO, "Initializing guest account");
		User guest = new User();
		guest.setUsername("guest");
		guest.setPassword("guest");
		guest.setRole("guest");
		guest.setFullname("Guest");
		guest.setEmail("guest@mok.local");
		guest.setTeamname("Masters of Code");
		em.merge(guest);
	}

	/**
	 * Gets the ID of the currently started competition.
	 * 
	 * @return The competition ID
	 */
	public Integer getCurrentCompetition() {
		return currentCompetitionId;
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
		Competition competition;
		competition = em.find(Competition.class, id);
		if (competition != null) {
			this.currentCompetitionId = competition.getId();
		} else {
			return false;
		}
		assignNodes();
		return true;
	}

	/**
	 * Stops the current competition. This will also stop the current round.
	 */
	public void stopCompetition() {
		timeService.stop();
		this.currentCompetitionId = null;
	}

	/**
	 * Gets the ID of the currently started round
	 * 
	 * @return Current round ID
	 */
	public Integer getCurrentRound() {
		return currentRoundId;
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
		Integer currentCompetitionId = getCurrentCompetition();
		if (currentCompetitionId == null) {
			return false;
		}
		Round round;
		try {
			round = em.find(Round.class, id);
		} catch (NumberFormatException nx) {
			return false;
		}
		if (round == null) {
			return false;
		}
		if (!currentCompetitionId.equals(round.getCompetition())) {
			return false;
		}
		this.hints = round.getAssignment().getHints();
		timeService.setClock(round.getDuration());
		timeService.start();
		this.currentRoundId = round.getId();
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
		if (!tick.isActive() && currentRoundId != null) {
			LOGGER.log(INFO, "Round #" + currentRoundId + " stopped");
			this.currentRoundId = null;
		}
		if (tick.isActive()) {
			int time = tick.getTotal() - tick.getRemaining();
			if (currentRoundId != null && hints != null) {
				for (Hint hint : hints) {
					if (time >= hint.getTime()) {
						hints.remove(hint);
						sendHint(hint);
					}
				}
			}
		}
	}

	/*
	 * TODO Nodes are assigned randomly over all users, not just the users in
	 * the current competition.
	 */
	private void assignNodes() {
		List<Integer> nodes = requestSender.getAvailableNodes();
		if (nodes.size() < 1) {
			LOGGER.log(WARNING, "No active nodes available!");
			return;
		}
		Random random = new Random();
		for (User user : userService.getUsers()) {
			if (user.getRole().equals("team")) {
				int nodeId = nodes.get((random.nextInt(nodes.size())));
				Node node = nodeService.getNode(nodeId);
				user.setNode(node);
				em.merge(user);
				LOGGER.log(INFO, "Assigned " + node + " to " + user);
			}
		}
	}

	private void sendHint(Hint hint) {
		router.broadcastHints(Collections.singletonList(hint));
	}
}