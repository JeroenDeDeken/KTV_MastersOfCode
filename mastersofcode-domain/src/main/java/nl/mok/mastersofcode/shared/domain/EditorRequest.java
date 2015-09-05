package nl.mok.mastersofcode.shared.domain;

import java.util.List;

/**
 * Request that can be requested by clients from the editor.
 * 
 * @author Jeroen Schepens
 *
 */
public class EditorRequest {

	/**
	 * Enumeration with all different types of requests.
	 * 
	 * @author Jeroen Schepens
	 */
	public enum RequestType {
		GET, SAVE, TEST, COMPILE, SUBMIT, HINT
	}

	private RequestType type;

	private List<String> parameters;

	/**
	 * Gets the type of request.
	 * 
	 * @return Request type
	 */
	public RequestType getType() {
		return type;
	}

	/**
	 * Sets the type of request.
	 * 
	 * @param type
	 *            Request type
	 */
	public void setType(RequestType type) {
		this.type = type;
	}

	/**
	 * Sets the request parameters.
	 * 
	 * @return Request parameters
	 */
	public List<String> getParameters() {
		return parameters;
	}

	/**
	 * Gets the request parameters.
	 * 
	 * @param parameters
	 *            Request parameters
	 */
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	@Override
	public String toString() {
		return "EditorRequest [type=" + type + ", parameters=" + parameters
				+ "]";
	}
}