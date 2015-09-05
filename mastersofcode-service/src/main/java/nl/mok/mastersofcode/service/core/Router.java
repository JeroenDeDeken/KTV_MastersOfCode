package nl.mok.mastersofcode.service.core;

import static java.util.logging.Level.INFO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.ejb.Singleton;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.websocket.Session;

import nl.mok.mastersofcode.service.domain.Hint;
import nl.mok.mastersofcode.service.domain.HintResult;
import nl.mok.mastersofcode.service.domain.NewsItem;
import nl.mok.mastersofcode.service.domain.Score;
import nl.mok.mastersofcode.service.rest.ScoreService;
import nl.mok.mastersofcode.shared.domain.EditorRequest;
import nl.mok.mastersofcode.shared.domain.EditorRequest.RequestType;
import nl.mok.mastersofcode.shared.domain.EditorResult;

import com.google.gson.Gson;

/**
 * Handles all incoming and outgoing JMS and websocket messages and routes them
 * to the correct destination. Routes EditorRequests received by the
 * ResultReceiver back to the requesting clients. Also sends NewsItems for some
 * result types and Hints to all clients.
 * <p>
 * The router keeps track of: Websocket sessions. Pending results. Pending
 * scores.
 * </p>
 * <p>
 * When a request is sent, the username of the requesting user will be stored in
 * a Map with the JMS message ID as key. When a result is received, it's JMS
 * correlation ID matches the JMS message ID of the original request. The result
 * will then be forwarded to the requesting user's websocket session.
 * </p>
 * <p>
 * When a SUBMIT request is sent, it's score will be calculated and stored in a
 * Map. If the result is successful, this score will be persisted.
 * </p>
 * <p>
 * When a request fails before it is sent to a node, this class will send back a
 * failed result immediately.
 * </p>
 * 
 * @author Jeroen Schepens
 */
@Singleton
public class Router {

	private final static Logger LOGGER = Logger.getLogger(Router.class
			.getName());

	@Inject
	private ScoreService scoreService;

	@Inject
	private Event<NewsItem> newsEvent;

	private Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();

	private Map<String, String> pendingResults = new HashMap<>();

	private Map<String, Score> pendingScores = new HashMap<>();

	private Map<String, Long> lastRequests = new HashMap<>();

	/**
	 * Stores a newly opened websocket session in a Map with the username as the
	 * key.
	 * 
	 * @param username
	 *            The user's username
	 * @param session
	 *            The newly opened websocket session
	 */
	public void openSession(String username, Session session) {
		sessions.put(username, session);
	}

	/**
	 * Removes a closed websocket session from the Map with sessions for a given
	 * user.
	 * 
	 * @param username
	 *            The user's username
	 */
	public void closeSession(String username) {
		sessions.remove(username);
	}

	/**
	 * Adds the requesting username for a given EditorRequest to the Map with
	 * pending results with the JMS message ID as the key. When the
	 * corresponding result is received, the result's JMS correlation ID will be
	 * the original request's JMS message ID. This ID is used to route the
	 * result back to the requesting client.
	 * 
	 * @param msgId
	 *            The JMS message ID of the sent request
	 * @param user
	 *            The username of the requesting user
	 */
	public void addPendingResult(String msgId, String user) {
		pendingResults.put(msgId, user);
	}

	/**
	 * Adds a calculated score to the Map with pending scores for a given SUBMIT
	 * request. When the result for this request is successful, this score will
	 * be persisted.
	 * 
	 * @param msgId
	 *            The JMS message ID of the sent SUBMIT request
	 * @param score
	 *            The amount of points scored should the SUBMIT request succeed
	 */
	public void addPendingScore(String msgId, Score score) {
		pendingScores.put(msgId, score);
	}

	/**
	 * Processes an EditorResult received by the ResultReceiver. If it's a
	 * successful SUBMIT result, the corresponding score will be persisted.
	 * 
	 * @param correlationId
	 *            The JMS message ID of the original request message
	 * @param message
	 *            The EditorResult in JSON format
	 * @throws IOException
	 *             Exception when sending to the websocket
	 */
	public void processResult(String correlationId, String message)
			throws IOException {
		String username = pendingResults.get(correlationId);
		if (username != null) {
			Gson gson = new Gson();
			EditorResult result = gson.fromJson(message, EditorResult.class);
			if (isExpensive(result.getType())) {
				lastRequests.put(username, 0L);
			}
			if (result.getType().equals(RequestType.SUBMIT)
					&& result.isSuccessful()) {
				try {
					processScore(correlationId);
				} catch (Exception ex) {
					return;
				}
			}
			broadcastResult(username, result);
			Session session = sessions.get(username);
			if (session != null) {
				session.getBasicRemote().sendText(message);
			}
		}
	}

	/**
	 * Sends a failed result back to the client in case an Exception occurs
	 * before the request is sent to the Build node.
	 * 
	 * @param username
	 *            The username that initiated the failed request
	 * @param result
	 *            The result of the failed request
	 */
	public void sendErrorResult(String username, EditorResult result) {
		Gson gson = new Gson();
		String message = gson.toJson(result);
		if (username != null) {
			Session session = sessions.get(username);
			if (session != null) {
				try {
					session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
	}

	private void processScore(String correlationId) {
		Score score = pendingScores.get(correlationId);
		score = scoreService.createScore(score);
		LOGGER.log(INFO, score.toString());
		broadcastNewsItem(score.toString());
	}

	private void broadcastResult(String username, EditorResult result) {
		String news;
		if (result.isSuccessful()) {
			news = username + " successfully runned a " + result.getType();
		} else {
			news = username + " failed a " + result.getType();
		}
		broadcastNewsItem(news);
	}

	private void broadcastNewsItem(String news) {
		NewsItem newsItem = new NewsItem(news);
		newsEvent.fire(newsItem);
	}

	/**
	 * Sends a list with Hints to all clients. Usually this is a list containing
	 * one item, but when a client connects when some hints are already
	 * released, the list contains all these hints.
	 * 
	 * @param hints
	 *            List with hints that will be sent to all client.
	 */
	public void broadcastHints(List<Hint> hints) {
		HintResult hintResult = new HintResult();
		hintResult.setConsoleOutput("Hints received");
		hintResult.setHints(hints);
		hintResult.setSuccessful(true);
		hintResult.setType(RequestType.HINT);
		for (Session session : sessions.values()) {
			session.getAsyncRemote().sendObject(hintResult);
		}
	}

	/**
	 * Checks if a given username can run an expensive request. A user cannot
	 * run a request if it is expensive and an expensive request is already
	 * processing. If no reply to an expensive request is received within a
	 * minute, a new request can be sent.
	 * 
	 * @param username
	 *            The username of the user to check
	 * @return True if the user can handle the request
	 */
	public boolean canHandleRequest(String username) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastRequests.getOrDefault(username, 0L) > 60000L) {
			lastRequests.put(username, currentTime);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if a given request type is expensive. Expensive requests can not
	 * be run simultaneously to prevent server overload.
	 * 
	 * @param type
	 *            The given request type
	 * @return True if the reques type is expensive
	 */
	public boolean isExpensive(EditorRequest.RequestType type) {
		return (type.equals(RequestType.COMPILE)
				|| type.equals(RequestType.TEST) || type
					.equals(RequestType.SUBMIT));
	}
}