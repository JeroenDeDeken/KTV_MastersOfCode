package nl.mok.mastersofcode.service.rest;

import java.util.List;

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

import nl.mok.mastersofcode.service.domain.Node;
import nl.mok.mastersofcode.service.messaging.RequestSender;

/**
 * REST endpoint which provides CRUD operations for Node objects.
 * 
 * @author Jeroen Schepens
 */
@Path("/nodes")
@Stateless
public class NodeService extends RestService<Integer, Node> {

	@Inject
	private RequestSender requestSender;

	@Override
	public Class<Node> getClazz() {
		return Node.class;
	}

	/**
	 * Returns a List with all Nodes.
	 * 
	 * @return List with all Nodes
	 */
	@GET
	@Produces("application/json")
	public List<Node> getNodes() {
		return findAll();
	}

	/**
	 * Finds a specific Node by it's ID. This method is not exposed by the REST
	 * API.
	 * 
	 * @param nodeId
	 *            The Node ID
	 * @return The found Node (if any, null otherwise)
	 */
	public Node getNode(int nodeId) {
		return find(nodeId);
	}

	/**
	 * Creates a new Node.
	 * 
	 * @param node
	 *            The Node to create
	 * @return The created Node
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Node createNode(Node node) {
		create(node);
		return node;
	}

	/**
	 * Updates a given Node.
	 * 
	 * @param node
	 *            The Node to update
	 * @return The updated Node
	 */

	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Node updateNode(Node node) {
		update(node);
		return node;
	}

	/**
	 * Deletes a given Node.
	 * 
	 * @param nodeId
	 *            The deleted Node's ID
	 */
	@DELETE
	@Path("/{id}")
	public void deleteNode(@PathParam("id") Integer nodeId) {
		delete(nodeId);
	}

	/**
	 * Reinitializes all Nodes. Useful when a new Node is added.
	 */
	@GET
	@Path("/reset")
	public void resetNodes() {
		requestSender.reinitialize();
	}
}