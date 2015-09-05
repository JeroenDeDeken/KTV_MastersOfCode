package nl.mok.mastersofcode.service.rest;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nl.mok.mastersofcode.service.core.CompetitionManager;
import nl.mok.mastersofcode.service.domain.Competition;
import nl.mok.mastersofcode.service.domain.Score;
import nl.mok.mastersofcode.service.domain.User;

/**
 * REST endpoint which provides CRUD operations for competition objects.
 * 
 * @author Jeroen Schepens
 */
@Path("/competitions")
@Stateless
public class CompetitionService extends RestService<Integer, Competition> {

	private final static Logger LOGGER = Logger
			.getLogger("nl.mok.mastersofcode.service");

	@Inject
	private CompetitionManager manager;

	@Inject
	private ScoreService scoreService;

	/**
	 * Returns a List with all competitions.
	 * 
	 * @return List with all competitions
	 */
	@GET
	@Produces("application/json")
	public List<Competition> getCompetitions() {
		return findAll();
	}

	/**
	 * Finds a specific competition by it's ID.
	 * 
	 * @param competitionId
	 *            The competition ID
	 * @return The found competition (if any, null otherwise)
	 */
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Competition getCompetition(@PathParam("id") Integer competitionId) {
		Competition competition = find(competitionId);
		return competition;
	}

	/**
	 * Gets the current competition (if any, null otherwise)
	 * 
	 * @return Current competition
	 */
	@GET
	@Path("/current")
	@Produces("application/json")
	public Competition getCurrentCompetition() {
		Integer competitionId = manager.getCurrentCompetition();
		if (competitionId != null) {
			Competition competition = find(manager.getCurrentCompetition());
			Map<String, Integer> scores = getScoresByTeam();
			for (User user : competition.getTeams()) {
				user.setTotalscore(scores.getOrDefault(user.getUsername(), 0));
			}
			return competition;
		} else {
			return null;
		}
	}

	/*
	 * TODO returns scores for all competitions. Should only return scores for
	 * the current competition.
	 */
	/**
	 * Gets all scores by team.
	 * 
	 * @return Map with all scores
	 */
	public Map<String, Integer> getScoresByTeam() {
		Map<String, Integer> scores = new HashMap<>();
		for (Score score : scoreService.getScores()) {
			String username = score.getUser().getUsername();
			scores.put(username,
					scores.getOrDefault(username, 0) + score.getScore());
		}
		return scores;
	}

	/**
	 * Starts a competition.
	 * 
	 * @param id
	 *            The ID of the competition to start
	 * @return True if the competition was successfully started
	 */
	@POST
	@Path("/current")
	@Consumes("text/plain")
	@Produces("application/json")
	public boolean setCurrentCompetition(String id) {
		int competitionId;
		try {
			competitionId = Integer.valueOf(id);
		} catch (NumberFormatException nx) {
			LOGGER.log(WARNING,
					"Competition # " + id + " not started: " + nx.getMessage());
			return false;
		}
		boolean started = manager.setCurrentCompetition(competitionId);
		LOGGER.log(INFO, "Competition #" + id + " started: " + started);
		return started;
	}

	/**
	 * Creates a new competition.
	 * 
	 * @param competition
	 *            The competition to create
	 * @return The created competition
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Competition createCompetition(Competition competition) {
		create(competition);
		return competition;
	}

	/**
	 * Updates a given competition.
	 * 
	 * @param competition
	 *            The competition to update
	 * @return The updated competition
	 */
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Competition updateCompetition(Competition competition) {
		update(competition);
		return competition;
	}

	/**
	 * Deletes a given competition.
	 * 
	 * @param competitionId
	 *            The deleted competition's ID
	 */
	@DELETE
	@Path("/{id}")
	public void deleteCompetition(@PathParam("id") Integer competitionId) {
		scoreService.purgeScoresByCompetition(find(competitionId));
		delete(competitionId);
	}

	/**
	 * Stops the current competition. If a round is started within the current
	 * competition, it will also be stopped.
	 */
	@GET
	@Path("/current/stop")
	public void stopCompetition() {
		LOGGER.log(INFO, "Competition stopped");
		manager.stopCompetition();
	}

	@Override
	public Class<Competition> getClazz() {
		return Competition.class;
	}
}