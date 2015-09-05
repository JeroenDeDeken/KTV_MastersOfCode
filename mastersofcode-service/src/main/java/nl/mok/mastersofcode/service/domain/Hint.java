package nl.mok.mastersofcode.service.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Hint domain class. A hint belongs to exactly one assignment. Hints will be
 * released after a given amount of time when a round is started with the
 * assignment the hint belongs to.
 * 
 * @author Jeroen Schepens
 */
@Entity
public class Hint implements Identifiable<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	private String assignment;

	@NotNull
	private String text;

	@NotNull
	private int time;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the assignment this hint belongs to.
	 * 
	 * @return Hint's assignment
	 */
	public String getAssignment() {
		return assignment;
	}

	/**
	 * Sets the assignment this hint belongs to.
	 * 
	 * @param assignment
	 *            Hint's assignment
	 */
	public void setAssignment(String assignment) {
		this.assignment = assignment;
	}

	/**
	 * Sets the text this hints contains.
	 * 
	 * @return Hint's text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Gets the text this hints contains.
	 * 
	 * @param text
	 *            Hint's text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the time in seconds after which the hint will be released.
	 * 
	 * @return Hint's release time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Gets the time in seconds after which the hint will be released.
	 * 
	 * @param time
	 *            Hint's release time
	 */
	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Hint [text=" + text + ", time=" + time + "]";
	}
}