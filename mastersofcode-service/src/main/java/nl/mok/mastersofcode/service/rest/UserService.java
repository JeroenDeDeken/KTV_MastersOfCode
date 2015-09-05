package nl.mok.mastersofcode.service.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import nl.mok.mastersofcode.service.domain.Competition;
import nl.mok.mastersofcode.service.domain.User;

/**
 * REST endpoint which provides CRUD operations for User objects.
 * 
 * @author Jeroen Schepens
 */
@Path("/users")
@Stateless
public class UserService extends RestService<String, User> {

	@Inject
	private ScoreService scoreService;

	@Inject
	private CompetitionService competitionService;

	/**
	 * Returns a List with all Users.
	 * 
	 * @return List with all Users
	 */
	@GET
	@Produces("application/json")
	public List<User> getUsers() {
		return findAll();
	}

	/**
	 * Finds a specific User by it's username.
	 * 
	 * @param username
	 *            The User's username
	 * @return The found User (if any)
	 */
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public User getUser(@PathParam("id") String username) {
		User user = find(username);
		if (user == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return user;
	}

	/**
	 * Gets the currently authenticated User.
	 * 
	 * @param context
	 *            The current security context
	 * @return The authenticated User
	 */
	@GET
	@Produces("application/json")
	@Path("/current")
	public User getCurrentUser(@Context SecurityContext context) {
		return find(context.getUserPrincipal().getName());
	}

	/**
	 * Creates a new User.
	 * 
	 * @param user
	 *            The User to create
	 * @return The created User
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public User createUser(User user) {
		create(user);
		return user;
	}

	/**
	 * Updates a given User.
	 * 
	 * @param user
	 *            The User to update
	 * @return The updated User
	 */
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public User editUser(User user) {
		update(user);
		return user;
	}

	/**
	 * Deletes a given user. All the User's associated Competition
	 * participations, Members and Scores will also be deleted.
	 * 
	 * @param username
	 *            The deleted User's username
	 */
	@DELETE
	@Path("/{id}")
	public void deleteUser(@PathParam("id") String username) {
		EntityManager em = getEntityManager();
		User user = find(username);
		scoreService.purgeScoresByUser(user);
		for (Competition competition : competitionService.getCompetitions()) {
			competition.getTeams().remove(user);
			competitionService.updateCompetition(competition);
		}
		Query memberQuery = em
				.createQuery("DELETE FROM Member m WHERE m.team = :p");
		memberQuery.setParameter("p", username);
		memberQuery.executeUpdate();
		em.flush();
		Query userQuery = getEntityManager().createQuery(
				"DELETE FROM User u WHERE u.username = :p");
		userQuery.setParameter("p", username);
		userQuery.executeUpdate();
	}

	@Override
	public Class<User> getClazz() {
		return User.class;
	}
}
