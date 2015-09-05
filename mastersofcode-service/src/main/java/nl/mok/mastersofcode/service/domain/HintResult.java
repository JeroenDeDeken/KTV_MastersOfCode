package nl.mok.mastersofcode.service.domain;

import java.util.List;

import nl.mok.mastersofcode.shared.domain.EditorResult;

/**
 * A hint result and contains a list with hints. It extends EditorResult and can
 * therefore be sent to clients over a websocket.
 * 
 * @author Jeroen Schepens
 */
public class HintResult extends EditorResult {

	private static final long serialVersionUID = 6431673858311573835L;

	private List<Hint> hints;

	/**
	 * Gets the list of hints
	 * 
	 * @return List with hints
	 */
	public List<Hint> getHints() {
		return hints;
	}

	/**
	 * Gets the list of hints
	 * 
	 * @param hints
	 *            List with hints
	 */
	public void setHints(List<Hint> hints) {
		this.hints = hints;
	}
}