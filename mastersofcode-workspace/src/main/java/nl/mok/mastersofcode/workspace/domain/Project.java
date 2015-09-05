package nl.mok.mastersofcode.workspace.domain;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Represents a single project on the filesystem. A project belongs to one team
 * and is a subdirectory within the team's workspace.
 * <p>
 * A project extends MavenBuild, submitting this to the workspace manager will
 * compile this project.
 * </p>
 * 
 * @author Jeroen Schepens
 */
public class Project extends MavenBuild {

	private static final List<String> COMPILE_GOAL = Collections
			.singletonList("compile");

	public Project(String team, String name) {
		super(team, name);
	}

	@Override
	public List<String> getGoals() {
		return COMPILE_GOAL;
	}

	@Override
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "Project [team=" + getTeam() + ", name=" + getName() + "]";
	}

}