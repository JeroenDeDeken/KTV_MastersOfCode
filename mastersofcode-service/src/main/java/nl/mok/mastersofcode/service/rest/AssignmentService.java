package nl.mok.mastersofcode.service.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nl.mok.mastersofcode.service.domain.Assignment;

/**
 * REST endpoint which provides CRUD operations for assignment objects.
 * 
 * @author Jeroen Schepens
 */
@Path("/assignments")
@Stateless
public class AssignmentService extends RestService<String, Assignment> {

	/**
	 * Returns a List with all assignments.
	 * 
	 * @return List with all assignments
	 */
	@GET
	@Produces("application/json")
	public List<Assignment> getAssignments() {
		return findAll();
	}

	/**
	 * Finds a specific assignment by it's ID.
	 * 
	 * @param assignmentID
	 *            The assignment ID
	 * @return The found assignment (if any, null otherwise)
	 */
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Assignment getAssignment(@PathParam("id") String assignmentID) {
		return find(assignmentID);
	}

	/**
	 * Creates a new assignment.
	 * 
	 * @param assignment
	 *            The assignment to create
	 * @return The created assignment
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Assignment createAssignment(Assignment assignment) {
		create(assignment);
		return assignment;
	}

	/**
	 * Updates a given assignment.
	 * 
	 * @param assignment
	 *            The assignment to update
	 * @return The updated assignment
	 */
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Assignment updateAssignment(Assignment assignment) {
		update(assignment);
		return assignment;
	}

	/**
	 * Deletes a given assignment.
	 * 
	 * @param assignmentId
	 *            The deleted assignment's ID
	 */
	@DELETE
	@Path("/{id}")
	public void deleteAssignment(@PathParam("id") String assignmentId) {
		delete(assignmentId);
	}

	@Override
	public Class<Assignment> getClazz() {
		return Assignment.class;
	}
}