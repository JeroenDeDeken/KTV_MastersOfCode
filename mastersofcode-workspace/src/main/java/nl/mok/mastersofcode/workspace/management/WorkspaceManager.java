package nl.mok.mastersofcode.workspace.management;

import static java.util.logging.Level.SEVERE;
import static nl.mok.mastersofcode.shared.util.ExceptionUtils.getStackTrace;
import static nl.mok.mastersofcode.workspace.domain.Constants.ASSIGNMENT_FILE;
import static nl.mok.mastersofcode.workspace.domain.Constants.POM_FILE;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import nl.mok.mastersofcode.shared.domain.BuildResult;
import nl.mok.mastersofcode.shared.domain.JavaFile;
import nl.mok.mastersofcode.shared.domain.TestCase;
import nl.mok.mastersofcode.shared.domain.meta.Assignment;
import nl.mok.mastersofcode.shared.domain.meta.Assignment.Tests.Test;
import nl.mok.mastersofcode.shared.domain.meta.AssignmentUnmarshaller;
import nl.mok.mastersofcode.workspace.domain.Artifact;
import nl.mok.mastersofcode.workspace.domain.Constants;
import nl.mok.mastersofcode.workspace.domain.MavenBuild;
import nl.mok.mastersofcode.workspace.domain.Project;
import nl.mok.mastersofcode.workspace.domain.SourceFile;
import nl.mok.mastersofcode.workspace.domain.TestFile;
import nl.mok.mastersofcode.workspace.domain.Workspace;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

/**
 * Workspace manager class. This class handles managing: workspaces, projects,
 * project artifacts, build jobs.
 * 
 * @author Jeroen
 */
public class WorkspaceManager {

	private final static Logger LOGGER = Logger
			.getLogger(WorkspaceManager.class.getName());

	/**
	 * The home directory of the user that runs the workspace management
	 * process.
	 */
	private final String home;

	/**
	 * The path to the maven executable
	 */
	private final String mavenHome;

	/**
	 * Constructs a new WorkspaceManager object. This will also initialize the
	 * workspace management directories on the filesystem.
	 * 
	 * @param mavenHome
	 *            The path to Apache Maven
	 */
	public WorkspaceManager(String mavenHome) {
		if (mavenHome == null) {
			throw new NullPointerException("Maven home cannot be null");
		}
		this.mavenHome = mavenHome;
		this.home = System.getProperty("user.home");
		new File(getArtifactsDir()).mkdirs();
		new File(getWorkspaceDir()).mkdirs();
	}

	/**
	 * Creates a workspace for a team. The workspace will be a subdirectory
	 * within the workspaces directory. This subdirectory will have the same
	 * name as the team's identifying name.
	 * 
	 * @param team
	 *            The team's identifying name
	 */
	public void createWorkspace(String team) {
		new File(getWorkspaceDir() + '/' + team).mkdirs();
	}

	/**
	 * Initializes an artifact into a workspace. The artifact is a zip file that
	 * will be extracted into the subfolder of a workspace.
	 * 
	 * @param artifact
	 *            The artifact to initialize
	 * @param workspace
	 *            The workspace to extract the artifact into
	 */
	public void initializeProject(Artifact artifact, Workspace workspace) {
		String artifactPath = getArtifactsDir() + '/' + artifact.getFilename();
		Project newProject = new Project(workspace.getTeam(),
				artifact.getArtifactName());
		String projectPath = getProjectDir(newProject);
		new File(projectPath).mkdirs();
		try {
			Unzipper unzipper = new Unzipper();
			unzipper.unzip(artifactPath, projectPath);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Initializes a project on the file system. It will extract an artifact's
	 * content to a given subfolder within a project.
	 * 
	 * @param project
	 *            The project to initialize
	 */
	public void initializeProject(Project project) {
		String artifactPath = getArtifactsDir() + '/' + project.getName()
				+ ".zip";
		String projectPath = getProjectDir(project);
		new File(projectPath).mkdirs();
		try {
			Unzipper unzipper = new Unzipper();
			unzipper.unzip(artifactPath, projectPath);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Runs a Maven Build
	 * 
	 * @param build
	 *            The build to run
	 * @return Exit code of the maven process
	 */
	public BuildResult runBuild(MavenBuild build) {
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile(new File(getPomFile(build)));
		request.setGoals(build.getGoals());
		request.setProperties(build.getProperties());
		Invoker invoker = new DefaultInvoker();
		invoker.setMavenHome(new File(mavenHome));
		BuildOutputHandler handler = new BuildOutputHandler();
		invoker.setOutputHandler(handler);
		InvocationResult result;
		int exitCode;
		long time = System.currentTimeMillis();
		String consoleOutput;
		boolean successful = false;
		try {
			result = invoker.execute(request);
			exitCode = result.getExitCode();
			consoleOutput = handler.toString();
			if (exitCode == 0) {
				successful = true;
			}
		} catch (MavenInvocationException mx) {
			String stackTrace = getStackTrace(mx);
			LOGGER.log(SEVERE, stackTrace);
			exitCode = -100;
			consoleOutput = stackTrace;
		} finally {
			time = System.currentTimeMillis() - time;
		}
		BuildResult buildResult = new BuildResult();
		buildResult.setTime(time);
		buildResult.setConsoleOutput(consoleOutput);
		buildResult.setExitCode(exitCode);
		buildResult.setSuccessful(successful);
		return buildResult;
	}

	/**
	 * Gets a list of all artifacts present is the artifacts folder.
	 * 
	 * @return List with all artifacts
	 */
	public List<Artifact> getArtifacts() {
		File file = new File(getArtifactsDir());
		String[] files = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return !new File(current, name).isDirectory();
			}
		});
		List<String> strings = Arrays.asList(files);
		List<Artifact> artifacts = new ArrayList<>();
		for (String artifact : strings) {
			artifacts.add(new Artifact(artifact));
		}
		return artifacts;
	}

	/**
	 * Loads an artifacts into the artifacts folder.
	 * 
	 * @param artifactName
	 *            The name of the artifact (file name)
	 * @param bytes
	 *            The artifact's bytes
	 */
	public void loadArtifact(String artifactName, byte[] bytes) {
		String filePath = getArtifactsDir() + '/' + artifactName;
		try {
			FileUtils.writeByteArrayToFile(new File(filePath), bytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets a list of all workspaces available on the filesystem.
	 * 
	 * @return List with strings of all workspaces directories
	 */
	public List<Workspace> getWorkspaces() {
		File file = new File(getWorkspaceDir());
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		List<String> strings = Arrays.asList(directories);
		List<Workspace> workspaces = new ArrayList<>();
		for (String team : strings) {
			workspaces.add(new Workspace(team));
		}
		return workspaces;
	}

	/**
	 * Checks if a given project exists on the file system
	 * 
	 * @param project
	 *            The project to check
	 * @return True if the project exists, false otherwise
	 */
	public boolean projectExists(Project project) {
		String projectDir = getProjectDir(project);
		File dir = new File(projectDir);
		return dir.exists();
	}

	/**
	 * Deletes a workspace from the filesystem
	 * 
	 * @param workspace
	 *            The workspace to delete
	 */
	public void deleteWorkspace(Workspace workspace) {
		try {
			FileUtils.deleteDirectory(new File(getWorkspaceDir() + '/'
					+ workspace.getTeam()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets a list of all projects within a workspace of a given team. A project
	 * is a single subdirectory within a team's workspace.
	 * 
	 * @param workspace
	 *            The workspace to search
	 * @return List with all projects within the workspace
	 */
	public List<Project> getProjects(Workspace workspace) {
		File file = new File(getWorkspaceDir() + '/' + workspace.getTeam());
		String[] files = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		List<String> directories = Arrays.asList(files);
		List<Project> projects = new ArrayList<>();
		for (String directory : directories) {
			projects.add(new Project(workspace.getTeam(), directory));
		}
		return projects;
	}

	/**
	 * Gets all source files for a given project.
	 * 
	 * @param project
	 *            The project to get the source files for
	 * @return List with all source files
	 */
	public Map<String, SourceFile> getSourceFiles(Project project) {
		Assignment assignment = getAssignment(project);
		Map<String, SourceFile> sourceFiles = new HashMap<>();
		for (Assignment.Files.File file : assignment.getFiles().getFile()) {
			sourceFiles.put(FilenameUtils.getBaseName(file.getPath()),
					new SourceFile(file.getPath(), file.isWrite(), project));
		}
		return sourceFiles;
	}

	/**
	 * Gets the JavaFile (file with content) for a given SourceFile (stub).
	 * 
	 * @param sourceFile
	 *            The SourceFile to read
	 * @return The JavaFile with content
	 * @throws IOException
	 *             Exception thrown at IO failure
	 */
	public JavaFile readSourceFile(SourceFile sourceFile) throws IOException {
		String filePath = getSourceFilePath(sourceFile);
		String content = FileUtils.readFileToString(new File(filePath));
		JavaFile javaFile = new JavaFile();
		String fileName = FilenameUtils.getBaseName(sourceFile.getFilePath());
		javaFile.setFilename(fileName);
		javaFile.setContent(content);
		javaFile.setWritable(sourceFile.isWritable());
		return javaFile;
	}

	/**
	 * Overwrites a source file with a given string to the file system.
	 * 
	 * @param sourceFile
	 *            The source file to overwrite
	 * @param content
	 *            The content to write
	 */
	public void writeSourceFile(SourceFile sourceFile, String content) {
		if (!sourceFile.isWritable()) {
			throw new RuntimeException("Source file must be writable");
		}
		String filePath = getSourceFilePath(sourceFile);
		try {
			FileUtils.writeByteArrayToFile(new File(filePath),
					content.getBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets all test cases for a given project.
	 * 
	 * @param project
	 *            The project to get the tests for
	 * @return The project's test cases
	 */
	public List<TestCase> getTests(Project project) {
		List<TestCase> tests = new ArrayList<>();
		Assignment assignment = getAssignment(project);
		for (Test test : assignment.getTests().getTest()) {
			tests.add(new TestCase(test.getName(), test.getDescription()));
		}
		return tests;
	}

	/**
	 * Gets a single test case for a given project.
	 * 
	 * @param project
	 *            The project to get the tests for
	 * @param testName
	 *            The name of the test case
	 * @return The test case
	 */
	public TestFile getTestFile(Project project, String testName) {
		Assignment assignment = getAssignment(project);
		for (Test test : assignment.getTests().getTest()) {
			if (testName.equals(test.getName())) {
				return new TestFile(project, testName);
			}
		}
		return null;
	}

	/**
	 * Gets the absolute path of the artifacts directory.
	 * 
	 * @return The absolute path of the artifacts directory
	 */
	private String getArtifactsDir() {
		return home + Constants.getArtifactsDir();
	}

	/**
	 * Gets the absolute path of the workspaces directory.
	 * 
	 * @return The absolute path of the workspaces directory
	 */
	private String getWorkspaceDir() {
		return home + Constants.getWorkspacesDir();
	}

	/**
	 * Gets the absolute path to the directory of a given project.
	 * 
	 * @param project
	 *            The project to get the directory for
	 * @return The path to the project's directory
	 */
	private String getProjectDir(MavenBuild build) {
		return getWorkspaceDir() + '/' + build.getTeam() + '/'
				+ build.getName();
	}

	/**
	 * Gets the absolute path to the pom.xml for a given project.
	 * 
	 * @param project
	 *            The project get the pom.xml path for
	 * @return The path to the pom.xml file
	 */
	private String getPomFile(MavenBuild build) {
		return getProjectDir(build) + '/' + POM_FILE;
	}

	/**
	 * Returns the absolute path of a source file.
	 * 
	 * @param sourceFile
	 *            The source file
	 * @return The absolute path to the source file
	 */
	private String getSourceFilePath(SourceFile sourceFile) {
		return getProjectDir(sourceFile.getProject()) + '/'
				+ sourceFile.getFilePath();
	}

	/**
	 * Gets the assignment metadata object for a given project.
	 * 
	 * @param project
	 *            The project to get the metadata for
	 * @return The project's assignment metadata
	 */
	private Assignment getAssignment(Project project) {
		String filename = getProjectDir(project) + '/' + ASSIGNMENT_FILE;
		AssignmentUnmarshaller unmarshaller = new AssignmentUnmarshaller(
				filename);
		return unmarshaller.unmarshall();
	}
}