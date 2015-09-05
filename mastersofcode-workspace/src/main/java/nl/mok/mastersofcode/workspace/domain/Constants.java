package nl.mok.mastersofcode.workspace.domain;

/**
 * Class containing constants that are used by the workspace manager.
 * 
 * @author Jeroen Schepens
 */
public class Constants {

	/**
	 * The name of the workspace manager's base directory
	 */
	private static final String BASE_DIR = "mastersofcode";

	/**
	 * The name of the workspace manager's artifacts directory
	 */
	private static final String ARTIFACTS_DIR = "artifacts";

	/**
	 * The name of the workspace manager's workspaces directory
	 */
	private static final String WORKSPACES_DIR = "workspaces";

	/**
	 * The name of a maven POM file
	 */
	public static final String POM_FILE = "pom.xml";

	public static final String ASSIGNMENT_FILE = "src/main/resources/META-INF/assignment.xml";

	/**
	 * Private constructor to prevent instantiation of this class.
	 */
	private Constants() {
	}

	/**
	 * Gets the relative directory to the arifacts directory.
	 * 
	 * @return The relative directory to the arifacts directory
	 */
	public static final String getArtifactsDir() {
		return '/' + BASE_DIR + '/' + ARTIFACTS_DIR;
	}

	/**
	 * Gets the relative directory to the workspaces directory.
	 * 
	 * @return The relative directory to the workspaces directory
	 */
	public static final String getWorkspacesDir() {
		return '/' + BASE_DIR + '/' + WORKSPACES_DIR;
	}
}