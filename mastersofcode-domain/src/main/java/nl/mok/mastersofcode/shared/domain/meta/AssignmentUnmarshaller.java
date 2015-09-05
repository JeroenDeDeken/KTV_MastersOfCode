package nl.mok.mastersofcode.shared.domain.meta;

import java.io.FileInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * Unmarshals an assignment xml file using a file name.
 * 
 * @author Jeroen Schepens
 */
public class AssignmentUnmarshaller {

	private final String filename;

	public AssignmentUnmarshaller(String filename) {
		this.filename = filename;
	}

	/**
	 * Unmarshals an assignment xml file.
	 * 
	 * @return The unmarshalled assignment object
	 */
	public Assignment unmarshall() {
		try {
			Class<Assignment> clazz = Assignment.class;
			JAXBContext jc = JAXBContext.newInstance(Class.forName(clazz
					.getName()));
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			FileInputStream fileInputStream = new FileInputStream(filename);
			Assignment assignment = clazz.cast(unmarshaller
					.unmarshal(fileInputStream));
			return assignment;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}