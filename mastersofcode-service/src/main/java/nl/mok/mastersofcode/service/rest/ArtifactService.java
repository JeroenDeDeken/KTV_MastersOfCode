package nl.mok.mastersofcode.service.rest;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import nl.mok.mastersofcode.service.core.ArtifactManager;
import nl.mok.mastersofcode.service.domain.Assignment;
import nl.mok.mastersofcode.service.domain.Hint;
import nl.mok.mastersofcode.service.messaging.RequestSender;
import nl.mok.mastersofcode.service.util.AssignmentParser;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

/**
 * Service for uploading assignment artifacts to the service module. The
 * artifact (.zip file) can be uploaded with a multipart request.
 * <p>
 * When an assignment artifact is uploaded, the metadata will be stored in the
 * database and the file will be stored on the filesystem.
 * </p>
 * 
 * @author Jeroen Schepens
 */
@WebServlet("/api/artifacts")
@MultipartConfig
public class ArtifactService extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	private ArtifactManager artifactManager;

	@Inject
	private AssignmentService assignmentService;

	@Inject
	private RequestSender requestSender;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Part filePart = request.getPart("file");
			String filename = getFileName(filePart);
			InputStream fileContent = filePart.getInputStream();
			byte[] bytes = IOUtils.toByteArray(fileContent);
			fileContent.close();
			String baseName = FilenameUtils.getBaseName(filename);
			Assignment assignment = AssignmentParser.parseAssignment(bytes);
			assignment.setArtifact(baseName);
			for (Hint hint : assignment.getHints()) {
				hint.setAssignment(baseName);
			}
			assignmentService.updateAssignment(assignment);
			artifactManager.writeArtifact(filename, bytes);
			requestSender.sendArtifact(filename, bytes);
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		} catch (Exception ex) {
			response.getWriter().write(ex.getMessage());
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		}
	}

	private static String getFileName(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				String fileName = cd.substring(cd.indexOf('=') + 1).trim()
						.replace("\"", "");
				return fileName.substring(fileName.lastIndexOf('/') + 1)
						.substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
			}
		}
		return null;
	}
}