package nl.mok.mastersofcode.shared.domain;

public class BuildResult extends EditorResult {

	private static final long serialVersionUID = -6381026790080040331L;

	private int exitCode = -200;
	private long time;

	public int getExitCode() {
		return exitCode;
	}

	public void setExitCode(int exitCode) {
		this.exitCode = exitCode;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "BuildResult [exitCode=" + exitCode + ", time=" + time + "]";
	}
}