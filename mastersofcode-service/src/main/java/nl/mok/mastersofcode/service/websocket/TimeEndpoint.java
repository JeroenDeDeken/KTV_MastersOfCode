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

import nl.mok.mastersofcode.service.domain.ClockTick;
import nl.mok.mastersofcode.service.util.TickEncoder;

/**
 * Websocket Endpoint that will send ClockTicks to all clients every second.
 * While the clock is paused or frozen, the same tick will be sent every second
 * until the clock is resumed.
 * 
 * @author Jeroen Schepens
 */
@Singleton
@ServerEndpoint(value = "/time", encoders = { TickEncoder.class })
public class TimeEndpoint {

	private static final Set<Session> sessions = Collections
			.synchronizedSet(new HashSet<Session>());

	/**
	 * Called when a session is opened.
	 * 
	 * @param session
	 *            The opened session
	 */
	@OnOpen
	public void onOpen(final Session session) {
		sessions.add(session);
	}

	/**
	 * Called when a session is closed.
	 * 
	 * @param session
	 *            The closed session
	 */
	@OnClose
	public void onClose(final Session session) {
		sessions.remove(session);
	}

	/**
	 * Observer method that will be called when a ClockTick is received. When a
	 * ClockTick is received, it will be sent to all connected clients.
	 * 
	 * @param tick
	 *            The received ClockTick
	 */
	public void onTick(@Observes ClockTick tick) {
		try {
			synchronized (sessions) {
				for (Session s : sessions) {
					s.getAsyncRemote().sendObject(tick);
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}