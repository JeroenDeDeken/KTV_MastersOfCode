package nl.mok.mastersofcode.shared.domain;

import java.util.List;

/**
 * Editor request to save files. This type of request contains a list of java
 * files that will be sent to a build node and saved in the workspace on it's
 * file system.
 * 
 * @author Jeroen Schepens
 */
public class WriteRequest extends EditorRequest {

	private List<JavaFile> files;

	/**
	 * Gets the list of files to save.
	 * 
	 * @return List of files
	 */
	public List<JavaFile> getFiles() {
		return files;
	}

	/**
	 * Sets the list of files to save.
	 * 
	 * @param files
	 *            List of files
	 */
	public void setFiles(List<JavaFile> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "WriteRequest [files=" + files + ", type=" + getType()
				+ ", parameters=" + getParameters() + "]";
	}
}