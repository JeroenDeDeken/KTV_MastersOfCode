package nl.mok.mastersofcode.workspace.domain;

/**
 * Represents a single workspace associated with a team. A workspace is a folder
 * on the filesystem which containts projects.
 * 
 * @author Jeroen Schepens
 */
public class Workspace {

	private final String team;

	/**
	 * Constructs a new workspace based on the team's name.
	 * 
	 * @param team
	 *            The team's name
	 */
	public Workspace(String team) {
		this.team = team;
	}

	/**
	 * Get the name of the team that owns this workspace.
	 * 
	 * @return Workspace's team
	 */
	public String getTeam() {
		return team;
	}
}