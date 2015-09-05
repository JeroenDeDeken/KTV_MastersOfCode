package nl.mok.mastersofcode.workspace.domain;

/**
 * Represents a single source file in a project on the file system. The
 * difference between a JavaFile and a SourceFile is that a JavaFile object
 * contains the file's content while a SourceFile object is just a stub to the
 * file.
 * 
 * @author Jeroen
 */
public class SourceFile {

	private final String filePath;
	private final boolean writable;
	private final Project project;

	/**
	 * Constructs a new source file.
	 * 
	 * @param filePath
	 *            The file path of the source file
	 * @param writable
	 *            Indicates if the file is writable
	 * @param project
	 *            The project this file belongs to
	 */
	public SourceFile(String filePath, boolean writable, Project project) {
		this.filePath = filePath;
		this.writable = writable;
		this.project = project;
	}

	/**
	 * Gets the file path of this source file.
	 * 
	 * @return The file path
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Checks is this source file if writable.
	 * 
	 * @return True if writable, false if not
	 */
	public boolean isWritable() {
		return writable;
	}

	/**
	 * Gets the project this file belongs to.
	 * 
	 * @return The parent project
	 */
	public Project getProject() {
		return project;
	}

	@Override
	public String toString() {
		return "SourceFile [filePath=" + filePath + ", writable=" + writable
				+ ", project=" + project + "]";
	}
}