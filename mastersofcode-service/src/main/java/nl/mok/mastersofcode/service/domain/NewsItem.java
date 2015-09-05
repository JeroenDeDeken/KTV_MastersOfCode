package nl.mok.mastersofcode.service.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * News items are notifications that are being sent to all connected clients.
 * 
 * @author Jeroen Schepens
 *
 */
public class NewsItem {

	private final String time;
	private final String text;

	/**
	 * Constructs a new news item based on a string with content. The time will
	 * automatically be set to the current time (format hh:mm).
	 * 
	 * @param text
	 *            Item content
	 */
	public NewsItem(String text) {
		this.text = text;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
		time = LocalDateTime.now().format(formatter).toString();
	}

	/**
	 * Gets the time at which the item was created. Format = hh:mm.
	 * 
	 * @return Item creation time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Gets the content of the news item.
	 * 
	 * @return Item content
	 */
	public String getText() {
		return text;
	}
}