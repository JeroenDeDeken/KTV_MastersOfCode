package nl.mok.mastersofcode.service.util;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import nl.mok.mastersofcode.service.domain.ClockTick;

import com.google.gson.Gson;

/**
 * Encoder that encodes a given ClockTick into a JSON string.
 * 
 * @author Jeroen Schepens
 */
public class TickEncoder implements Encoder.Text<ClockTick> {

	@Override
	public void init(EndpointConfig config) {
		return;
	}

	@Override
	public void destroy() {
		return;
	}

	@Override
	public String encode(ClockTick object) throws EncodeException {
		Gson gson = new Gson();
		return gson.toJson(object);
	}
}