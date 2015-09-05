package nl.mok.mastersofcode.shared.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Util class to get a trowable's stack trace as a string.
 * 
 * @author Jeroen Schepens
 */
public class ExceptionUtils {

	/**
	 * Gets the strack trace of a throwable and converts it into a string.
	 * 
	 * @param throwable
	 *            The throwable to get the stack trace for
	 * @return The stack trace as string
	 */
	public static String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		throwable.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}