package nl.mok.mastersofcode.service.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Competition domain class. A competition contains rounds and a list of
 * participating teams.
 * 
 * @author Jeroen Schepens
 */
@Entity
public class Competition implements Serializable, Identifiable<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@Size(min = 1, max = 30)
	private String title;

	@NotNull
	private String description;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<User> teams;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "competition", orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Round> rounds;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the title of this competition.
	 * 
	 * @return Competition title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of this competition.
	 * 
	 * @param title
	 *            Competition title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the description of this competition.
	 * 
	 * @return Competition description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of this competition.
	 * 
	 * @param description
	 *            Competition description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the list of participating teams.
	 * 
	 * @return Participating teams
	 */
	public Set<User> getTeams() {
		return teams;
	}

	/**
	 * Sets the list of participating teams.
	 * 
	 * @param teams
	 *            Participating teams
	 */
	public void setTeams(Set<User> teams) {
		this.teams = teams;
	}

	/**
	 * Gets the list of rounds in this competition.
	 * 
	 * @return Rounds in this competition
	 */
	public Set<Round> getRounds() {
		return rounds;
	}

	/**
	 * Sets the list of rounds in this competition.
	 * 
	 * @param rounds
	 *            Rounds in this competition
	 */
	public void setRounds(Set<Round> rounds) {
		this.rounds = rounds;
	}
}