package nl.mok.mastersofcode.builder;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static nl.mok.mastersofcode.shared.util.ExceptionUtils.getStackTrace;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import nl.mok.mastersofcode.workspace.management.WorkspaceManager;

/**
 * Uploads assignment artifacts to the file system.
 * 
 * @author Jeroen Schepens
 */
@Stateless
public class UploadService {

	private final static Logger LOGGER = Logger.getLogger(UploadService.class
			.getName());

	private WorkspaceManager workspaceManager;

	@PostConstruct
	private void init() {
		String mavenHome = System.getProperty("maven.home");
		workspaceManager = new WorkspaceManager(mavenHome);
	}

	/**
	 * Uploads an assignment artifacts to the artifacts folder within the file
	 * system.
	 * 
	 * @param filename
	 *            The filename of the assignment artifact
	 * @param bytes
	 *            The artifact's content
	 */
	public void uploadArtifact(String filename, byte[] bytes) {
		LOGGER.log(INFO, "Uploading artifact with name \"" + filename + "\".");
		try {
			workspaceManager.loadArtifact(filename, bytes);
			LOGGER.log(INFO, "Artifact with name \"" + filename
					+ "\" uploaded.");
		} catch (RuntimeException rx) {
			LOGGER.log(SEVERE, getStackTrace(rx));
		}
	}
}