package nl.mok.mastersofcode.service.rest;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

import java.util.List;
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
import nl.mok.mastersofcode.service.domain.Round;

/**
 * REST endpoint which provides CRUD operations for Round objects.
 * 
 * @author Jeroen Schepens
 */
@Path("/rounds")
@Stateless
public class RoundService extends RestService<Integer, Round> {

	private final static Logger LOGGER = Logger
			.getLogger("nl.mok.mastersofcode.service");

	@Inject
	private CompetitionManager manager;

	@Inject
	private ScoreService scoreService;

	/**
	 * Returns a List with all Rounds.
	 * 
	 * @return List with all Rounds
	 */
	@GET
	@Produces("application/json")
	public List<Round> getRounds() {
		return findAll();
	}

	/**
	 * Finds a specific Round by it's ID.
	 * 
	 * @param roundId
	 *            The Round's ID
	 * @return The found Round (if any, null otherwise)
	 */
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Round getRound(@PathParam("id") Integer roundId) {
		return find(roundId);
	}

	/**
	 * Gets the current Round.
	 * 
	 * @return The current Round (if any, null otherwise)
	 */
	@GET
	@Produces("application/json")
	@Path("/current")
	public Round getCurrentRound() {
		Integer competitionId = manager.getCurrentRound();
		if (competitionId != null) {
			return find(competitionId);
		} else {
			return null;
		}
	}

	/**
	 * Sets the current Round. A Round can only be started if the Competition it
	 * belongs to is also started.
	 * 
	 * @param id
	 *            The ID of the Round to set
	 * @return Boolean indicating if the Round was successfully set
	 */
	@POST
	@Consumes("text/plain")
	@Produces("application/json")
	@Path("/current")
	public boolean setCurrentRound(String id) {
		int roundId;
		try {
			roundId = Integer.valueOf(id);
		} catch (NumberFormatException nx) {
			LOGGER.log(WARNING,
					"Round # " + id + " not started: " + nx.getMessage());
			return false;
		}
		boolean started = manager.setCurrentRound(roundId);
		LOGGER.log(INFO, "Round #" + id + " started: " + started);
		return manager.setCurrentRound(roundId);
	}

	/**
	 * Creates a new Round.
	 * 
	 * @param round
	 *            The Round to create
	 * @return The created Round
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Round createRound(Round round) {
		create(round);
		return round;
	}

	/**
	 * Updates a given Round.
	 * 
	 * @param round
	 *            The Round to update
	 * @return The updated Round
	 */
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Round updateRound(Round round) {
		update(round);
		return round;
	}

	/**
	 * Deletes a given Round.
	 * 
	 * @param roundId
	 *            The deleted Round's ID
	 */
	@DELETE
	@Path("/{id}")
	public void deleteRound(@PathParam("id") Integer roundId) {
		scoreService.purgeScoresByRound(find(roundId));
		delete(roundId);
	}

	@Override
	public Class<Round> getClazz() {
		return Round.class;
	}
}