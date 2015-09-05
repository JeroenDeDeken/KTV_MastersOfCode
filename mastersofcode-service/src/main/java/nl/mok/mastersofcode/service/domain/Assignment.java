package nl.mok.mastersofcode.service.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 * Assignment domain class. Contains source files, test, hints and assignment
 * metadata.
 * <p>
 * The data of an assignment is parsed from
 * "src/main/resources/META-INF/assignment.xml" within an assignment artifact.
 * </p>
 * 
 * @author Jeroen Schepens
 */
@Entity
public class Assignment implements Identifiable<String> {

	@Id
	private String artifact;

	@NotNull
	private String name;

	@NotNull
	private String participantDescription;

	@NotNull
	private String spectatorDescription;

	@NotNull
	private String creatorName;

	@NotNull
	private String creatorOrganisation;

	@NotNull
	private String creatorLink;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "assignment", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<Hint> hints;

	@Override
	public String getId() {
		return artifact;
	}

	@Override
	public void setId(String id) {
		this.artifact = id;
	}

	/**
	 * Gets the artifact identifier of this assignment. An artifact identifier
	 * is the name of the artifact without the .zip (or .jar) extension. This is
	 * also the name of this assignment project folder within a workspace.
	 * 
	 * @return The artifact identifier
	 */
	public String getArtifact() {
		return artifact;
	}

	/**
	 * Sets the artifact identifier of this assignment. An artifact identifier
	 * is the name of the artifact without the .zip (or .jar) extension. This is
	 * also the name of this assignment project folder within a workspace.
	 * 
	 * @param artifact
	 *            The artifact identifier
	 */
	public void setArtifact(String artifact) {
		this.artifact = artifact;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the participant description of this assignment. This description
	 * will be shown to the participants.
	 * 
	 * @return The participant description
	 */
	public String getParticipantDescription() {
		return participantDescription;
	}

	/**
	 * Gets the participant description of this assignment. This description
	 * will be shown to the participants.
	 * 
	 * @param participantDescription
	 *            The participant description
	 */
	public void setParticipantDescription(String participantDescription) {
		this.participantDescription = participantDescription;
	}

	/**
	 * Sets the spectator description of this assignment. This description will
	 * be shown to the spectators.
	 * 
	 * @return The spectator description
	 */
	public String getSpectatorDescription() {
		return spectatorDescription;
	}

	/**
	 * Sets the spectator description of this assignment. This description will
	 * be shown to the spectators.
	 * 
	 * @param spectatorDescription
	 *            The spectator description
	 */
	public void setSpectatorDescription(String spectatorDescription) {
		this.spectatorDescription = spectatorDescription;
	}

	/**
	 * Gets the name of this assignment's creator.
	 * 
	 * @return The name of the creator
	 */
	public String getCreatorName() {
		return creatorName;
	}

	/**
	 * Sets the name of this assignment's creator.
	 * 
	 * @param creatorName
	 *            The name of the creator
	 */
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	/**
	 * Gets the name of this assignment's creator organisation.
	 * 
	 * @return The name of the organisation
	 */
	public String getCreatorOrganisation() {
		return creatorOrganisation;
	}

	/**
	 * Sets the name of this assignment's creator organisation.
	 * 
	 * @param creatorOrganisation
	 *            The name of the organisation
	 */
	public void setCreatorOrganisation(String creatorOrganisation) {
		this.creatorOrganisation = creatorOrganisation;
	}

	/**
	 * Gets the URL to the website of this assignment's creator.
	 * 
	 * @return The URL to the creator's website
	 */
	public String getCreatorLink() {
		return creatorLink;
	}

	/**
	 * Sets the URL to the website of this assignment's creator.
	 * 
	 * @param creatorLink
	 *            The URL to the creator's website
	 */
	public void setCreatorLink(String creatorLink) {
		this.creatorLink = creatorLink;
	}

	/**
	 * Get a set with this assignment's hints.
	 * 
	 * @return A set with hints
	 */
	public Set<Hint> getHints() {
		return hints;
	}

	/**
	 * Set a set with this assignment's hints.
	 * 
	 * @param hints
	 *            A set with hints
	 */
	public void setHints(Set<Hint> hints) {
		this.hints = hints;
	}
}