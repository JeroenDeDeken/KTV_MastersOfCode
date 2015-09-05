package nl.mok.mastersofcode.service.core;

import java.io.File;
import java.io.IOException;

import javax.ejb.Singleton;

import org.apache.commons.io.FileUtils;

/**
 * Manages assignment artifacts on the file system. Assignment artifacts can be
 * written to the file system and read from the file system.
 * <p>
 * The artifacts are stored in the artifact repository, a subdirectory within
 * the user's home folder.
 * </p>
 * 
 * @author Jeroen Schepens
 */
@Singleton
public class ArtifactManager {

	private static final String ARTIFACT_DIRECTORY = "/mastersofcode/artifacts/repository";

	private final String home = System.getProperty("user.home");

	/**
	 * Writes an assignment artifact to the artifact repository folder.
	 * 
	 * @param fileName
	 *            the artifacts file name
	 * @param bytes
	 *            The artifact's bytes
	 */
	public void writeArtifact(String fileName, byte[] bytes) {
		String filePath = home + ARTIFACT_DIRECTORY + '/' + fileName;
		System.out.println(filePath);
		try {
			FileUtils.writeByteArrayToFile(new File(filePath), bytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Reads an assignment artifact's bytes into a byte array.
	 * 
	 * @param fileName
	 *            The artifact's filename
	 * @return Byte array with the artifact's bytes
	 * @throws IOException
	 *             Exception thrown at IO failure
	 */
	public byte[] readArtifact(String fileName) throws IOException {
		return FileUtils.readFileToByteArray(new File(home + ARTIFACT_DIRECTORY
				+ fileName + ".zip"));
	}
}