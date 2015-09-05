package nl.mok.mastersofcode.service.domain;

/**
 * Interface for domain classes to uniformly get and set their identifying
 * properties. REST endpoints use this to pass ID's to the EntityManager to find
 * entities.
 * 
 * @author Jeroen Schepens
 *
 * @param <T>
 *            Type of the identifying property.
 */
public interface Identifiable<T> {

	/**
	 * Gets the identifying property of this object.
	 * 
	 * @return Identifying property
	 */
	public T getId();

	/**
	 * Sets the identifying property of this object.
	 * 
	 * @param id
	 *            Identifying property
	 */
	public void setId(T id);
}