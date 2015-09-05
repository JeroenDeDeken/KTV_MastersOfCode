package nl.mok.mastersofcode.service.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import nl.mok.mastersofcode.service.domain.Assignment;
import nl.mok.mastersofcode.service.domain.Hint;
import nl.mok.mastersofcode.shared.domain.meta.AssignmentStreamUnmarshaller;

/**
 * Parses assignment artifacts and converts the metadata into Assignment
 * objects.
 * 
 * @author Jeroen Schepens
 */
public class AssignmentParser {

	private static final String ASSIGNMENT_XML = "src/main/resources/META-INF/assignment.xml";

	/**
	 * Reads an assignment artifact as a byte array and converts it's metadata
	 * into an Assignment object.
	 * 
	 * @param bytes
	 *            The assignment artifact as byte array
	 * @return The Assignment metadata object
	 */
	public static Assignment parseAssignment(byte[] bytes) {
		nl.mok.mastersofcode.shared.domain.meta.Assignment assignment = loadAssignment(bytes);
		return convertAssignment(assignment);
	}

	private static Assignment convertAssignment(
			nl.mok.mastersofcode.shared.domain.meta.Assignment input) {
		Assignment assignment = new Assignment();
		assignment.setName(input.getMetadata().getName());
		assignment.setCreatorName(input.getMetadata().getCreator().getName());
		assignment.setCreatorOrganisation(input.getMetadata().getCreator()
				.getOrganisation());
		assignment.setCreatorLink(input.getMetadata().getCreator().getLink());
		assignment.setName(input.getMetadata().getName());
		assignment.setParticipantDescription(input.getMetadata()
				.getParticipants());
		assignment.setSpectatorDescription(input.getMetadata().getSpectators());
		Set<Hint> hints = new HashSet<>();
		for (nl.mok.mastersofcode.shared.domain.meta.Assignment.Hints.Hint hint : input
				.getHints().getHint()) {
			Hint h = new Hint();
			h.setText(hint.getText());
			h.setTime(hint.getTime());
			hints.add(h);
		}
		assignment.setHints(hints);
		return assignment;
	}

	private static nl.mok.mastersofcode.shared.domain.meta.Assignment loadAssignment(
			byte[] bytes) {
		InputStream inputStream;
		try {
			inputStream = getAssignmentInputStream(bytes);
			AssignmentStreamUnmarshaller asum = new AssignmentStreamUnmarshaller(
					inputStream);
			nl.mok.mastersofcode.shared.domain.meta.Assignment assignment = asum
					.unmarshall();
			return assignment;
		} catch (IOException e) {
			return null;
		}
	}

	private static InputStream getAssignmentInputStream(byte[] bytes)
			throws IOException {
		InputStream inputStream = new ByteArrayInputStream(bytes);
		ZipInputStream zipIn = new ZipInputStream(inputStream);
		ZipEntry entry = zipIn.getNextEntry();
		while (entry != null) {
			if (entry.getName().equals(ASSIGNMENT_XML)) {
				return zipIn;
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		throw new RuntimeException("Cannot find assignment in zip");
	}
}