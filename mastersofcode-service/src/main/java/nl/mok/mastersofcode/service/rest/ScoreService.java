package nl.mok.mastersofcode.service.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nl.mok.mastersofcode.service.domain.Competition;
import nl.mok.mastersofcode.service.domain.Round;
import nl.mok.mastersofcode.service.domain.Score;
import nl.mok.mastersofcode.service.domain.User;

/**
 * REST endpoint which provides operations for Score objects.
 * 
 * @author Jeroen Schepens
 */
@Path("/scores")
@Stateless
public class ScoreService extends RestService<Integer, Score> {

	/**
	 * Returns a List with all Scores.
	 * 
	 * @return List with all Scores
	 */
	@GET
	@Produces("application/json")
	public List<Score> getScores() {
		return findAll();
	}

	/**
	 * Creates a new Score.
	 * 
	 * @param score
	 *            The score to create
	 * @return The created score
	 */
	public Score createScore(Score score) {
		create(score);
		return score;
	}

	/**
	 * Deletes all Scores from a given Round. This method is called prior to the
	 * deletion of this Round.
	 * 
	 * @param round
	 *            The Round to delete the scores from
	 */
	public void purgeScoresByRound(Round round) {
		List<Score> scores = findAll();
		for (Score score : scores) {
			if (score.getRound().getId().equals(round.getId())) {
				delete(score.getId());
			}
		}
	}

	/**
	 * Deletes all Scores from a given Competition. This method is called prior
	 * to the deletion of this Competition.
	 * 
	 * @param competition
	 *            The Competition to delete the scores from
	 */
	public void purgeScoresByCompetition(Competition competition) {
		for (Round round : competition.getRounds()) {
			purgeScoresByRound(round);
		}
	}

	/**
	 * Deletes all Scores from a given User. This method is called prior to the
	 * deletion of this User.
	 * 
	 * @param user
	 *            The User to delete the scores from
	 */
	public void purgeScoresByUser(User user) {
		List<Score> scores = findAll();
		for (Score score : scores) {
			if (score.getUser().getUsername().equals(user.getUsername())) {
				delete(score.getId());
			}
		}
	}

	@Override
	public Class<Score> getClazz() {
		return Score.class;
	}
}