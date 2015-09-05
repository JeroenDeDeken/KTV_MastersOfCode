package nl.mok.mastersofcode.workspace;

//import java.io.File;
//import java.io.IOException;


import nl.mok.mastersofcode.workspace.management.WorkspaceManager;

//import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

public class WorkspaceManagerTest {

	@Test(expectedExceptions = NullPointerException.class)
	public void testMavenHomeNull() {
		new WorkspaceManager(null);
	}

	/*
	@Test
	public void testLol() throws IOException {
		WorkspaceManager wsm = new WorkspaceManager(
				"C:/Users/Jeroen/apache-maven-3.2.1-bin/apache-maven-3.2.1");

		String artifactFile = "C:/Users/Jeroen/zooi.zip";
		byte[] artifactBytes = FileUtils.readFileToByteArray(new File(
				artifactFile));
		wsm.loadArtifact("test", artifactBytes);

		wsm.createWorkspace("teamberry");
		wsm.createWorkspace("teamjas-on");
		wsm.createWorkspace("patricode");

		for (Workspace workspace : wsm.getWorkspaces()) {
			for (Artifact artifact : wsm.getArtifacts()) {
				wsm.initializeProject(artifact, workspace);
			}
		}

		for (Workspace workspace : wsm.getWorkspaces()) {
			for (Project project : wsm.getProjects(workspace)) {
				for (TestCase test : wsm.getTests(project)) {
					System.out.println(wsm.runBuild(test));
				}
				System.out.println(wsm.runBuild(project));
				for (SourceFile sourceFile : wsm.getSourceFiles(project)) {
					if (sourceFile.isWritable()) {
						byte[] bytes = "roflcopter".getBytes();
						wsm.writeSourceFile(sourceFile, bytes);
					}
				}
				System.out.println(wsm.runBuild(project));
			}
		}
	}
	*/
}