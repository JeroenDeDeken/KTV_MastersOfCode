package nl.mok.mastersofcode.shared.domain;

import com.google.gson.Gson;

/**
 * Decodes an editor request from JSON format to a java object.
 * 
 * @author Jeroen Schepens
 */
public class RequestDecoder {

	private final Gson gson = new Gson();

	/**
	 * Decodes an editor request.
	 * 
	 * @param json
	 *            The request in JSON format to decode
	 * @return The decoded request object
	 */
	public EditorRequest decode(String json) {
		EditorRequest request = gson.fromJson(json, EditorRequest.class);
		if (request.getType() == EditorRequest.RequestType.SAVE) {
			return gson.fromJson(json, WriteRequest.class);
		}
		return request;
	}
}