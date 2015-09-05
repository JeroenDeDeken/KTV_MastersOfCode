package nl.mok.mastersofcode.service.util;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import nl.mok.mastersofcode.service.domain.NewsItem;

import com.google.gson.Gson;

/**
 * Encodes a news item into JSON messages.
 * 
 * @author Jeroen Schepens
 */
public class NewsItemEncoder implements Encoder.Text<NewsItem> {

	private final Gson gson = new Gson();

	@Override
	public void init(EndpointConfig config) {
		return;
	}

	@Override
	public void destroy() {
		return;
	}

	@Override
	public String encode(NewsItem object) throws EncodeException {
		return gson.toJson(object);
	}
}