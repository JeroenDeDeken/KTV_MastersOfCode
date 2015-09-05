package nl.mok.mastersofcode.service.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;

/**
 * A member represents a participant that can be part of a team. A team is
 * represented by it's team leader and has a list of members. The team leader
 * (and therefore a team) is an object of the class User.
 * 
 * @author Jeroen Schepens
 *
 */
@XmlRootElement
@Entity
public class Member implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2738700087352472247L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@Size(min = 1, max = 30)
	private String team;

	@NotNull
	@Size(min = 1, max = 30)
	private String membername;

	@NotNull
	@Email
	private String email;

	/**
	 * Gets this member's ID. This is an auto generated integer.
	 * 
	 * @return Member ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets this member's ID. This is an auto generated integer.
	 * 
	 * @param id
	 *            Member ID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the username of the team this member belongs to (username of the
	 * team leader).
	 * 
	 * @return Team (leader)'s username
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * Gets the username of the team this member belongs to (username of the
	 * team leader).
	 * 
	 * @param team
	 *            Team (leader)'s username
	 */
	public void setTeam(String team) {
		this.team = team;
	}

	/**
	 * Gets the full name of this member.
	 * 
	 * @return Member's full name
	 */
	public String getMembername() {
		return membername;
	}

	/**
	 * Sets the full name of this member.
	 * 
	 * @param membername
	 *            Member's full name
	 */
	public void setMembername(String membername) {
		this.membername = membername;
	}

	/**
	 * Gets the email address of this member.
	 * 
	 * @return Member's email address
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Gets the email address of this member.
	 * 
	 * @param email
	 *            Member's email address
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Member other = (Member) obj;
		if (id != other.id)
			return false;
		return true;
	}
}