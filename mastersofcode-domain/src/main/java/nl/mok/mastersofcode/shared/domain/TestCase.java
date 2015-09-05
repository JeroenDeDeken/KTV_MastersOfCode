package nl.mok.mastersofcode.shared.domain;

/**
 * A test case is a single TestNG compatible java test file.
 * 
 * @author Jeroen Schepens
 */
public class TestCase {

	private String name;

	private String description;

	public TestCase() {
	}

	/**
	 * Constructs a new test case.
	 * 
	 * @param name
	 *            The name of the TestNG compatible java test file
	 * @param description
	 *            The description of the test case
	 */
	public TestCase(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * The name of the TestNG compatible java test file
	 * 
	 * @return The name of the test file
	 */
	public String getName() {
		return name;
	}

	/**
	 * The name of the TestNG compatible java test file
	 * 
	 * @param name
	 *            The name of the test file
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the description of the test case
	 * 
	 * @return The description of the test case
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the test case
	 * 
	 * @param description
	 *            The description of the test case
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "TestCase [name=" + name + ", description=" + description + "]";
	}
}