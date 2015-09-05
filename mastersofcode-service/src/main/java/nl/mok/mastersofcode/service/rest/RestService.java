package nl.mok.mastersofcode.service.rest;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import nl.mok.mastersofcode.service.domain.Identifiable;

/**
 * Abstract generic base class for REST endpoints for domain classes. This class
 * provides CRUD methods and uses an EntityManager. The domain classes must be
 * JPA Entities and implement the Identifiable interface. <br>
 * This class provides methods for the following operations:
 * 
 * <ul>
 * <li>Find all objects</li>
 * <li>Find an object by ID</li>
 * <li>Persist an object</li>
 * <li>Update an object</li>
 * <li>Delete an object</li>
 * </ul>
 * <p>
 * This class also provides an method to expose the EntityManager to manually
 * execute JPA queries.
 * </p>
 * 
 * @author Jeroen Schepens
 *
 * @param <ID>
 *            The type of the identifier of the domain class
 * @param <E>
 *            The type of the domain class
 */
public abstract class RestService<ID, E extends Identifiable<ID>> {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Gets the class type of the domain class that is managed by the
	 * implementing service. The class is passed to the EntityManager at runtime
	 * to find objects of this class.
	 * 
	 * @return Domain class managed by implementing service
	 */
	public abstract Class<E> getClazz();

	/**
	 * Return a list with all instances of this object.
	 * 
	 * @return List with all objects
	 */
	protected List<E> findAll() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(getClazz());
		Root<E> rootEntry = cq.from(getClazz());
		CriteriaQuery<E> all = cq.select(rootEntry);
		TypedQuery<E> allQuery = em.createQuery(all);
		return allQuery.getResultList();
	}

	/**
	 * Finds a single object for a given identifier.
	 * 
	 * @param id
	 *            The object's identifier
	 * @return The found object
	 */
	protected E find(ID id) {
		return em.find(getClazz(), id);
	}

	/**
	 * Persists an object.
	 * 
	 * @param object
	 *            The object to persist
	 */
	protected void create(E object) {
		em.persist(object);
	}

	/**
	 * Updates an object.
	 * 
	 * @param object
	 *            The object to update
	 */
	protected void update(E object) {
		em.merge(object);
	}

	/**
	 * Deletes an object.
	 * 
	 * @param id
	 *            The ID of the object to delete
	 */
	protected void delete(ID id) {
		E object = find(id);
		em.remove(object);
	}

	/**
	 * Exposes the EntityManager to manually execute JPA queries.
	 * 
	 * @return The EntityManager
	 */
	protected EntityManager getEntityManager() {
		return this.em;
	}
}