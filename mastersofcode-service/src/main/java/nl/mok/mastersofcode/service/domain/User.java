package nl.mok.mastersofcode.service.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;

/**
 * User domain class.<br>
 * A user can have different roles:
 * <ul>
 * <li>"admin"</li>
 * <li>"team"</li>
 * <li>"guest"</li>
 * </ul>
 * <p>
 * If the user has the role "team", the user represents the team leader of his
 * team. The list with members represent the rest of the team.
 * </p>
 * 
 * @author Jeroen Schepens
 */
@XmlRootElement
@Entity
public class User implements Serializable, Identifiable<String> {

	private static final long serialVersionUID = 1L;

	@Id
	@Pattern(regexp = "^[a-z0-9]*$")
	@Size(min = 1, max = 20)
	private String username;

	@NotNull
	private String password;

	@NotNull
	@Size(min = 1, max = 30)
	private String fullname;

	@Column(unique = true)
	@Size(min = 1, max = 40)
	private String teamname;

	@NotNull
	@Email
	private String email;

	@NotNull
	private String role;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "team")
	private Set<Member> members;

	@ManyToOne
	@JoinColumn(name = "NODE")
	private Node node;

	@Transient
	private int totalscore;

	/**
	 * Gets this user's username.
	 * 
	 * @return The username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets this user's username.
	 * 
	 * @param username
	 *            The username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets this user's password
	 * 
	 * @return The password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets this user's password
	 * 
	 * @param password
	 *            The password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets this user's full name
	 * 
	 * @return The full name
	 */
	public String getFullname() {
		return fullname;
	}

	/**
	 * Gets this user's full name
	 * 
	 * @param fullname
	 *            The full name
	 */
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	/**
	 * Sets this users team name. This only applies to users that are team
	 * leaders.
	 * 
	 * @return The team name
	 */
	public String getTeamname() {
		return teamname;
	}

	/**
	 * Sets this users team name. This only applies to users that are team
	 * leaders.
	 * 
	 * @param teamname
	 *            The team name.
	 */
	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	/**
	 * Gets this user's email address.
	 * 
	 * @return The email address
	 */
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets this user's role. Possible values are: "admin", "team" and "guest"
	 * 
	 * @return The role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets this user's role. Possible values are: "admin", "team" and "guest"
	 * 
	 * @param role
	 *            The role
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Gets the members that belong to this user.
	 * 
	 * @return The members
	 */
	public Set<Member> getMembers() {
		return members;
	}

	/**
	 * Sets the members that belong to this user.
	 * 
	 * @param members
	 *            The members
	 */
	public void setMembers(Set<Member> members) {
		this.members = members;
	}

	/**
	 * Gets this user's Node
	 * 
	 * @return The Node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * Sets this user's Node.
	 * 
	 * @param node
	 *            The Node
	 */
	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * Gets this user's total score. The total score is not persisted but
	 * calculated at runtime.
	 * 
	 * @return The total score
	 */
	public int getTotalscore() {
		return totalscore;
	}

	/**
	 * Sets this user's total score. The total score is not persisted but
	 * calculated at runtime.
	 * 
	 * @param totalscore
	 *            The total score
	 */
	public void setTotalscore(int totalscore) {
		this.totalscore = totalscore;
	}

	@Override
	public String getId() {
		return this.getUsername();
	}

	@Override
	public void setId(String id) {
		this.setUsername(id);
	}

	@Override
	public String toString() {
		if (teamname != null) {
			return teamname;
		} else {
			return fullname;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}