package nl.mok.mastersofcode.workspace.management;

import org.apache.maven.shared.invoker.InvocationOutputHandler;

/**
 * Build output handler that stores all console output lines of a maven build in
 * a string buffer. The toString method returns it's content.
 * 
 * @author Jeroen Schepens
 */
public class BuildOutputHandler implements InvocationOutputHandler {

	private final StringBuffer stringBuffer;

	public BuildOutputHandler() {
		this.stringBuffer = new StringBuffer();
	}

	@Override
	public void consumeLine(String line) {
		stringBuffer.append(line + '\n');
	}

	@Override
	public String toString() {
		return stringBuffer.toString();
	}
}