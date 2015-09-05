package nl.mok.mastersofcode.service.websocket;

import java.io.IOException;
import java.security.Principal;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import nl.mok.mastersofcode.service.core.Router;
import nl.mok.mastersofcode.service.domain.User;
import nl.mok.mastersofcode.service.messaging.RequestSender;
import nl.mok.mastersofcode.service.rest.UserService;
import nl.mok.mastersofcode.service.util.EditorDecoder;
import nl.mok.mastersofcode.service.util.EditorEncoder;
import nl.mok.mastersofcode.shared.domain.EditorRequest;

/**
 * Websocket endpoint that receives editor requests from clients and sends back
 * results to them.
 * <p>
 * When a session is opened, it will be stored in the router.
 * </p>
 * <p>
 * When a request is received, it will be passed to the request sender. The
 * router will also send back the response to the requesting client.
 * </p>
 * 
 * @author Raymond (aka Anton)
 */
@ServerEndpoint(value = "/editor", encoders = { EditorEncoder.class }, decoders = { EditorDecoder.class })
public class EditorEndpoint {

	@Inject
	private RequestSender requestSender;

	@Inject
	private Router router;

	@Inject
	private UserService userService;

	/**
	 * Called when a message is received from the client. This message is
	 * decoded into a editor request.
	 * <p>
	 * The message will be sent to a build node by the request sender
	 * </p>
	 * 
	 * @param client
	 *            The session of the requesting client
	 * @param message
	 *            The editor request sent by the client
	 */
	@OnMessage
	public void onMessage(final Session client, final EditorRequest message) {
		Principal principal = client.getUserPrincipal();
		User user = userService.getUser(principal.getName());
		requestSender.sendRequest(user, message);
	}

	/**
	 * Called when a new session is opened. The session will be stored in the
	 * router.
	 * 
	 * @param session
	 *            The newly opened session
	 */
	@OnOpen
	public void onOpen(final Session session) {
		Principal principal = session.getUserPrincipal();
		String username = principal.getName();
		if (username != null) {
			router.openSession(username, session);
		} else {
			try {
				session.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Called when a session is closed. The session will be removed from the
	 * router.
	 * 
	 * @param session
	 *            The closed session
	 */
	@OnClose
	public void onClose(final Session session) {
		Principal principal = session.getUserPrincipal();
		String username = principal.getName();
		router.closeSession(username);
	}
}