package nl.mok.mastersofcode.shared.domain;

import java.io.Serializable;

/**
 * Result to an editor request.
 * 
 * @author Jeroen Schepens
 */
public class EditorResult implements Serializable {

	private static final long serialVersionUID = 2623396991186112494L;

	private boolean successful;

	private String id;

	private String consoleOutput;

	private EditorRequest.RequestType type;

	/**
	 * Gets the JMS message ID of the originating request.
	 * 
	 * @return Originating message ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the JMS message ID of the originating request.
	 * 
	 * @param id
	 *            Originating message ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the console output of this result.
	 * 
	 * @return Console output
	 */
	public String getConsoleOutput() {
		return consoleOutput;
	}

	/**
	 * Sets the console output of this result.
	 * 
	 * @param consoleOutput
	 *            Console output
	 */
	public void setConsoleOutput(String consoleOutput) {
		this.consoleOutput = consoleOutput;
	}

	/**
	 * Sets the type of originating request.
	 * 
	 * @return Orignal request type
	 */
	public EditorRequest.RequestType getType() {
		return type;
	}

	/**
	 * Gets the type of originating request.
	 * 
	 * @param type
	 *            Orignal request type
	 */
	public void setType(EditorRequest.RequestType type) {
		this.type = type;
	}

	/**
	 * Checks if this result is successful.
	 * 
	 * @return True if successful, false if not
	 */
	public boolean isSuccessful() {
		return successful;
	}

	/**
	 * Sets if this result is successful.
	 * 
	 * @param successful
	 *            True if successful, false if not
	 */
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	@Override
	public String toString() {
		return "EditorResult [successful=" + successful + ", id=" + id
				+ ", type=" + type + "]";
	}
}