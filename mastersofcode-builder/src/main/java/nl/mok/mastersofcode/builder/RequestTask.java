package nl.mok.mastersofcode.builder;

import static java.util.logging.Level.SEVERE;
import static nl.mok.mastersofcode.shared.util.ExceptionUtils.getStackTrace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import nl.mok.mastersofcode.shared.domain.BuildResult;
import nl.mok.mastersofcode.shared.domain.EditorRequest;
import nl.mok.mastersofcode.shared.domain.EditorResult;
import nl.mok.mastersofcode.shared.domain.GetResult;
import nl.mok.mastersofcode.shared.domain.JavaFile;
import nl.mok.mastersofcode.shared.domain.RequestDecoder;
import nl.mok.mastersofcode.shared.domain.WriteRequest;
import nl.mok.mastersofcode.shared.domain.EditorRequest.RequestType;
import nl.mok.mastersofcode.workspace.domain.MavenBuild;
import nl.mok.mastersofcode.workspace.domain.Project;
import nl.mok.mastersofcode.workspace.domain.SourceFile;
import nl.mok.mastersofcode.workspace.domain.TestCollection;
import nl.mok.mastersofcode.workspace.domain.TestFile;
import nl.mok.mastersofcode.workspace.management.WorkspaceManager;

/**
 * Task that processes an Editor Request.
 * 
 * @author Jeroen Schepens
 *
 */
public class RequestTask implements Callable<EditorResult> {

	private final static Logger LOGGER = Logger.getLogger(RequestTask.class
			.getName());

	private WorkspaceManager workspaceManager;

	private final RequestDecoder decoder = new RequestDecoder();

	private final String message;
	private final String messageId;
	private final String team;
	private final String projectName;

	/**
	 * Creates a new request task
	 * 
	 * @param message
	 *            The editor request as JSON message
	 * @param messageId
	 *            The JMS message ID of the request
	 * @param team
	 *            The ID of the requesting team
	 * @param projectName
	 *            The project to process
	 */
	public RequestTask(String message, String messageId, String team,
			String projectName) {
		this.message = message;
		this.messageId = messageId;
		this.team = team;
		this.projectName = projectName;
		String mavenHome = System.getProperty("maven.home");
		workspaceManager = new WorkspaceManager(mavenHome);
	}

	/**
	 * Gets the JMS message ID of the processed request
	 * 
	 * @return Message ID of the processed request
	 */
	public String getMessageId() {
		return messageId;
	}

	@Override
	public EditorResult call() {
		EditorRequest request = decoder.decode(message);
		EditorResult editorResult;
		switch (request.getType()) {
		case GET:
			editorResult = handleGet();
			break;
		case SAVE:
			editorResult = handleSave((WriteRequest) request);
			break;
		case COMPILE:
			editorResult = handleCompile();
			break;
		case TEST:
			editorResult = handleTest(request.getParameters());
			break;
		case SUBMIT:
			editorResult = handleSubmit();
			break;
		default:
			editorResult = null;
			break;
		}
		if (editorResult != null) {
			editorResult.setId(messageId);
		}
		return editorResult;
	}

	private EditorResult handleGet() {
		GetResult result = new GetResult();
		try {
			Project project = new Project(team, projectName);
			if (!workspaceManager.projectExists(project)) {
				workspaceManager.initializeProject(project);
			}
			// Load source files
			List<SourceFile> files = new ArrayList<>(workspaceManager
					.getSourceFiles(project).values());
			List<JavaFile> javaFiles = new ArrayList<>();
			result.setType(EditorRequest.RequestType.GET);
			result.setSuccessful(true);
			for (SourceFile file : files) {
				JavaFile javaFile = workspaceManager.readSourceFile(file);
				javaFiles.add(javaFile);
			}
			result.setFiles(javaFiles);
			// Load tests
			result.setTestCases(workspaceManager.getTests(project));
		} catch (Exception ex) {
			LOGGER.log(SEVERE, getStackTrace(ex));
			result.setSuccessful(false);
		}
		return result;
	}

	private EditorResult handleSave(WriteRequest writeRequest) {
		Project project = new Project(team, projectName);
		EditorResult editorResult = new EditorResult();
		editorResult.setType(RequestType.SAVE);
		editorResult.setSuccessful(true);
		try {
			Map<String, SourceFile> sourceFiles = workspaceManager
					.getSourceFiles(project);
			for (JavaFile file : writeRequest.getFiles()) {
				SourceFile sourceFile = sourceFiles.get(file.getFilename());
				if (sourceFile.isWritable()) {
					workspaceManager.writeSourceFile(sourceFile,
							file.getContent());
				}
			}
		} catch (Exception ex) {
			LOGGER.log(SEVERE, getStackTrace(ex));
			editorResult.setSuccessful(false);
		}
		return editorResult;
	}

	private EditorResult handleCompile() {
		Project project = new Project(team, projectName);
		if (!workspaceManager.projectExists(project)) {
			workspaceManager.initializeProject(project);
		}
		BuildResult buildResult = workspaceManager.runBuild(project);
		buildResult.setType(RequestType.COMPILE);
		return buildResult;
	}

	private EditorResult handleTest(List<String> testFiles) {
		Project project = new Project(team, projectName);
		if (!workspaceManager.projectExists(project)) {
			workspaceManager.initializeProject(project);
		}
		MavenBuild mavenBuild;
		if (testFiles.size() > 0) {
			String testFile = testFiles.get(0);
			TestFile test = workspaceManager.getTestFile(project, testFile);
			mavenBuild = test;
		} else {
			mavenBuild = new TestCollection(team, projectName);
		}
		BuildResult buildResult = workspaceManager.runBuild(mavenBuild);
		buildResult.setType(RequestType.TEST);
		return buildResult;
	}

	private EditorResult handleSubmit() {
		Project project = new Project(team, projectName);
		if (!workspaceManager.projectExists(project)) {
			workspaceManager.initializeProject(project);
		}
		MavenBuild mavenBuild;
		mavenBuild = new TestCollection(team, projectName);
		BuildResult buildResult = workspaceManager.runBuild(mavenBuild);
		buildResult.setType(RequestType.SUBMIT);
		return buildResult;
	}
}