package nl.mok.mastersofcode.service.util;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import nl.mok.mastersofcode.shared.domain.EditorRequest;
import nl.mok.mastersofcode.shared.domain.RequestDecoder;

/**
 * Decoder that decodes JSON messages received by the editor websocket endpoint
 * into editor requests. Editor requests are the messages that are being sent by
 * the client in the editor screen.
 * 
 * @author Raymond
 */
public class EditorDecoder implements Decoder.Text<EditorRequest> {

	private final RequestDecoder decoder = new RequestDecoder();

	@Override
	public void destroy() {
	}

	@Override
	public void init(EndpointConfig config) {
		return;
	}

	@Override
	public EditorRequest decode(String m) throws DecodeException {
		try {
			return decoder.decode(m);
		} catch (Throwable t) {
			throw new DecodeException(m, "JSON Decoding Error", t);
		}
	}

	@Override
	public boolean willDecode(String arg0) {
		return true;
	}
}