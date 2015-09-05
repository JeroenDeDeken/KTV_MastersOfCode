package nl.mok.mastersofcode.service.util;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import nl.mok.mastersofcode.shared.domain.EditorResult;

import com.google.gson.Gson;

/**
 * Encoder that encodes editor results into JSON messages. These messages are
 * responses to editor requests and will be sent back to the requesting
 * websocket endpoint.
 * 
 * @author Raymond
 */
public class EditorEncoder implements Encoder.Text<EditorResult> {

	@Override
	public void init(EndpointConfig config) {
		return;
	}

	@Override
	public void destroy() {
		return;
	}

	@Override
	public String encode(EditorResult object) throws EncodeException {
		Gson gson = new Gson();
		return gson.toJson(object);
	}
}