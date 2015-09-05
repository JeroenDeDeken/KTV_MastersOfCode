package nl.mok.mastersofcode.service.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Round represents a single round in a competition. An assignment can belong
 * to multiple rounds.
 * <p>
 * You spin me right round!
 * </p>
 * 
 * @author Jeroen Schepens
 */
@XmlRootElement
@Entity
public class Round implements Serializable, Identifiable<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	private int competition;

	@NotNull
	private int duration;

	@NotNull
	private int multiplier;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "assignment")
	private Assignment assignment;

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the ID of the Competition this Round belongs to.
	 * 
	 * @return The Competition ID
	 */
	public int getCompetition() {
		return competition;
	}

	/**
	 * Gets the ID of the Competition this Round belongs to.
	 * 
	 * @param competition
	 *            The Competition ID
	 */
	public void setCompetition(int competition) {
		this.competition = competition;
	}

	/**
	 * Gets the duration of this Round (seconds).
	 * 
	 * @return The duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Sets the duration of this Round (seconds).
	 * 
	 * @param duration
	 *            The duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * Gets the multiplier of this Round. The score is calculated by multiplying
	 * the remaining amount of seconds upon submit with this multiplier.
	 * 
	 * @return The multiplier
	 */
	public int getMultiplier() {
		return multiplier;
	}

	/**
	 * Sets the multiplier of this Round. The score is calculated by multiplying
	 * the remaining amount of seconds upon submit with this multiplier.
	 * 
	 * @param multiplier
	 *            The multiplier
	 */
	public void setMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}

	/**
	 * Gets this round's assignment.
	 * 
	 * @return The assignment
	 */
	public Assignment getAssignment() {
		return assignment;
	}

	/**
	 * Gets this round's assignment.
	 * 
	 * @param assignment
	 *            The assignment
	 */
	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	@Override
	public String toString() {
		return "Round #" + id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((assignment == null) ? 0 : assignment.hashCode());
		result = prime * result + competition;
		result = prime * result + duration;
		result = prime * result + id;
		result = prime * result + multiplier;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Round other = (Round) obj;
		if (assignment == null) {
			if (other.assignment != null)
				return false;
		} else if (!assignment.equals(other.assignment))
			return false;
		if (competition != other.competition)
			return false;
		if (duration != other.duration)
			return false;
		if (id != other.id)
			return false;
		if (multiplier != other.multiplier)
			return false;
		return true;
	}
}