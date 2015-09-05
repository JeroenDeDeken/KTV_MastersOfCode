package nl.mok.mastersofcode.service.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nl.mok.mastersofcode.service.domain.Hint;

/**
 * REST endpoint which provides CRUD operations for hint objects.
 * 
 * @author Jeroen Schepens
 */
public class HintService extends RestService<Integer, Hint> {

	/**
	 * Returns a List with all hints.
	 * 
	 * @return List with all hints
	 */
	@GET
	@Produces("application/json")
	public List<Hint> getHints() {
		return findAll();
	}

	/**
	 * Finds a specific hint by it's ID.
	 * 
	 * @param hintId
	 *            The hint ID
	 * @return The found hint (if any, null otherwise)
	 */
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Hint getHint(@PathParam("id") Integer hintId) {
		return find(hintId);
	}

	/**
	 * Creates a new hint.
	 * 
	 * @param hint
	 *            The hint to create
	 * @return The created hint
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Hint createHint(Hint hint) {
		create(hint);
		return hint;
	}

	/**
	 * Updates a given hint
	 * 
	 * @param hint
	 *            The hint to update
	 * @return The updated hint
	 */
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Hint updateHint(Hint hint) {
		update(hint);
		return hint;
	}

	/**
	 * Deletes a given hint.
	 * 
	 * @param hintId
	 *            The deleted hint's ID
	 */
	@DELETE
	@Path("/{id}")
	public void deleteHint(@PathParam("id") Integer hintId) {
		delete(hintId);
	}

	@Override
	public Class<Hint> getClazz() {
		return Hint.class;
	}
}