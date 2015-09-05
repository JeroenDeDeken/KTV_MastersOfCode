package nl.mok.mastersofcode.service.websocket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import nl.mok.mastersofcode.service.domain.NewsItem;
import nl.mok.mastersofcode.service.util.NewsItemEncoder;

/**
 * Websocket endpoint that sends newsfeed items to all clients.
 * 
 * @author Jeroen Schepens
 */
@Singleton
@ServerEndpoint(value = "/newsfeed", encoders = { NewsItemEncoder.class })
public class NewsFeedEndpoint {

	private static final Set<Session> sessions = Collections
			.synchronizedSet(new HashSet<Session>());

	/**
	 * Called when a session is opened. The session will be stored in a map
	 * containing all sessions.
	 * 
	 * @param session
	 *            The newly opened session
	 */
	@OnOpen
	public void onOpen(final Session session) {
		sessions.add(session);
	}

	/**
	 * Called when a session is closed. The session will be removed from the map
	 * containing all sessions.
	 * 
	 * @param session
	 *            The closed session
	 */
	@OnClose
	public void onClose(final Session session) {
		sessions.remove(session);
	}

	/**
	 * CDI observer method listening for news items. When a news item is
	 * received, it will be broadcasted to all clients.
	 * 
	 * @param item
	 *            The received news item
	 */
	public void onNewsItem(@Observes NewsItem item) {
		try {
			synchronized (sessions) {
				for (Session s : sessions) {
					s.getAsyncRemote().sendObject(item);
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}