package nl.mok.mastersofcode.workspace.domain;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Represents a collection of TestNG compatible tests for a project. This maven
 * build will execute all tests.
 * 
 * @author Jeroen Schepens
 */
public class TestCollection extends MavenBuild {

	/**
	 * Constructs a new test collection.
	 * 
	 * @param team
	 *            The username of the team that requested the test
	 * @param name
	 *            The name of the project that will be tested
	 */
	public TestCollection(String team, String name) {
		super(team, name);
	}

	private static final List<String> TEST_GOAL = Collections
			.singletonList("test");

	@Override
	public List<String> getGoals() {
		return TEST_GOAL;
	}

	@Override
	public Properties getProperties() {
		return null;
	}
}