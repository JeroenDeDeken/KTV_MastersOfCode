package nl.mok.mastersofcode.workspace.domain;

import java.util.List;
import java.util.Properties;

/**
 * Represent a maven build goal that can be executed by the workspace manager.
 * 
 * @author Jeroen Schepens
 */
public abstract class MavenBuild {

	private final String team;
	private final String name;

	/**
	 * Constructs a new maven build.
	 * 
	 * @param team
	 *            The username of the team that requested the build
	 * @param name
	 *            The name of the project that will be built
	 */
	public MavenBuild(String team, String name) {
		this.team = team;
		this.name = name;
	}

	/**
	 * Gets the username of the team that requested the build
	 * 
	 * @return Username of the team that requested the build
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * Get the name of the project that will be built
	 * 
	 * @return Name of the project that will be built
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the properties of this maven build (-D switches).
	 * 
	 * @return Build properties
	 */
	public abstract Properties getProperties();

	/**
	 * Gets the list of goals of this maven build.
	 * 
	 * @return List with build goals
	 */
	public abstract List<String> getGoals();
}