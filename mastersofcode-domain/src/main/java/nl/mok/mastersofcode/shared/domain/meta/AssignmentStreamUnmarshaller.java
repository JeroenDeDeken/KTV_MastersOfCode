package nl.mok.mastersofcode.shared.domain.meta;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * Unmarshals an assignment xml file using an input stream.
 * 
 * @author Jeroen Schepens
 */
public class AssignmentStreamUnmarshaller {

	private final InputStream inputStream;

	public AssignmentStreamUnmarshaller(InputStream inputStream) {
		this.inputStream = inputStream;
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
			Assignment assignment = clazz.cast(unmarshaller
					.unmarshal(inputStream));
			return assignment;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}