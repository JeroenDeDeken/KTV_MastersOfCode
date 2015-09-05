package nl.mok.mastersofcode.workspace.domain;

import org.apache.commons.io.FilenameUtils;

/**
 * Represents an artifact stored on the file system.
 * 
 * @author Jeroen Schepens
 */
public class Artifact {

	private final String filename;

	/**
	 * Constructs a new artifacts based on it's file name.
	 * 
	 * @param filename
	 *            The artifact's file name
	 */
	public Artifact(String filename) {
		this.filename = filename;
	}

	/**
	 * Gets the file name of this artifact.
	 * 
	 * @return The artifact's file name
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Gets the name of this artifact (file name without extension).
	 * 
	 * @return The artifact's name
	 */
	public String getArtifactName() {
		return FilenameUtils.removeExtension(filename);
	}
}