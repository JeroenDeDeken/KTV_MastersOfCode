package nl.mok.mastersofcode.builder;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.mok.mastersofcode.workspace.domain.Artifact;
import nl.mok.mastersofcode.workspace.domain.Project;
import nl.mok.mastersofcode.workspace.domain.Workspace;
import nl.mok.mastersofcode.workspace.management.WorkspaceManager;

/**
 * Servlet that returns diagnostic information for a build server. This servlet
 * acts as the homepage of a deployed builder.
 * 
 * @author Jeroen Schepens
 */
@WebServlet("/")
public class StatusServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String mavenHome = System.getProperty("maven.home");
		WorkspaceManager workspaceManager = new WorkspaceManager(mavenHome);
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		writer.write("MASTERS OF CODE BUILD SERVER\n\n");
		writer.write("ARTIFACTS\n\n");
		for (Artifact artifact : workspaceManager.getArtifacts()) {
			writer.write("  " + artifact.getFilename() + '\n');
		}
		writer.write("\nWORKSPACES\n");
		for (Workspace workspace : workspaceManager.getWorkspaces()) {
			writer.write("\n  " + workspace.getTeam() + '\n');
			for (Project project : workspaceManager.getProjects(workspace)) {
				writer.write("   - " + project.getName() + '\n');
			}
		}
		writer.close();
	}
}