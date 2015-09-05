package nl.mok.mastersofcode.workspace.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Represents the tests of a single TestNG compatible java file. This maven
 * build will test only this test case.
 * 
 * @author Jeroen Schepens
 */
public class TestFile extends MavenBuild {

	private static final String TEST_GOAL = "test";
	// private static final String TEST_PARAMETER = "-Dtest=%s";

	private final String testName;
	private final List<String> goals;

	/**
	 * Constructs a new test file
	 * 
	 * @param project
	 *            The project to which the test file belongs to
	 * @param testName
	 *            The file name of the TestNG compatible java file
	 */
	public TestFile(Project project, String testName) {
		super(project.getTeam(), project.getName());
		this.testName = testName;
		this.goals = new ArrayList<>();
		goals.add(TEST_GOAL);
		// goals.add(String.format(TEST_PARAMETER, testName));
	}

	/**
	 * Gets the name of the testcase.
	 * 
	 * @return Name of the testcase
	 */
	public String getTestName() {
		return testName;
	}

	@Override
	public List<String> getGoals() {
		return Collections.unmodifiableList(goals);
	}

	@Override
	public Properties getProperties() {
		Properties properties = new Properties();
		properties.put("test", testName);
		System.out.println("PROBZ : " + properties);
		return properties;
	}

	@Override
	public String toString() {
		return "TestBuild [goals=" + goals + ", team=" + getTeam() + ", name="
				+ getName() + "]";
	}
}