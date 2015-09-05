package nl.mok.mastersofcode.shared.domain;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a java file with content. The difference between a JavaFile and a
 * SourceFile is that a JavaFile object contains the file's content while a
 * SourceFile object is just a stub to the file.
 * 
 * @author Jeroen Schepens
 */
@XmlRootElement
public class JavaFile {

	private String filename;
	private String content;
	private boolean writable;

	/**
	 * Gets the file's filename
	 * 
	 * @return Filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Gets the file's filename.
	 * 
	 * @param filename
	 *            Filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * Gets the file's content.
	 * 
	 * @return File content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the file's content.
	 * 
	 * @param content
	 *            File content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Checks if this file is writable.
	 * 
	 * @return True if writable, false if read-only
	 */
	public boolean isWritable() {
		return writable;
	}

	/**
	 * Sets if this file is writable.
	 * 
	 * @param writable
	 *            True if writable, false if read-only
	 */
	public void setWritable(boolean writable) {
		this.writable = writable;
	}
}