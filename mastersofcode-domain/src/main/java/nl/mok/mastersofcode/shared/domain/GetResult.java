package nl.mok.mastersofcode.shared.domain;

import java.util.List;

/**
 * Result for a GET request. Contains java files and test cases.
 * 
 * @author Jeroen Schepens
 */
public class GetResult extends EditorResult {

	private static final long serialVersionUID = 4788961073999919110L;

	private List<JavaFile> files;

	private List<TestCase> testCases;

	/**
	 * Gets the list of java files in this result.
	 * 
	 * @return Java files
	 */
	public List<JavaFile> getFiles() {
		return files;
	}

	/**
	 * Sets the list of java files in this result.
	 * 
	 * @param files
	 *            Java files
	 */
	public void setFiles(List<JavaFile> files) {
		this.files = files;
	}

	/**
	 * Gets the list of test cases in this result.
	 * 
	 * @return Test cases
	 */
	public List<TestCase> getTestCases() {
		return testCases;
	}

	/**
	 * Sets the list of test cases in this result.
	 * 
	 * @param testCases
	 *            Test cases
	 */
	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}
}