package nl.mok.mastersofcode.service.messaging;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static nl.mok.mastersofcode.shared.util.ExceptionUtils.getStackTrace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang3.exception.ExceptionUtils;

import nl.mok.mastersofcode.service.core.Router;
import nl.mok.mastersofcode.service.core.TimeService;
import nl.mok.mastersofcode.service.domain.NewsItem;
import nl.mok.mastersofcode.service.domain.Node;
import nl.mok.mastersofcode.service.domain.Round;
import nl.mok.mastersofcode.service.domain.Score;
import nl.mok.mastersofcode.service.domain.User;
import nl.mok.mastersofcode.service.rest.NodeService;
import nl.mok.mastersofcode.service.rest.RoundService;
import nl.mok.mastersofcode.shared.domain.EditorRequest;
import nl.mok.mastersofcode.shared.domain.EditorRequest.RequestType;
import nl.mok.mastersofcode.shared.domain.EditorResult;

import com.google.gson.Gson;

/**
 * Sends EditorRequests to the Build nodes. This class also distributes
 * artifacts to build nodes.
 * 
 * @author Jeroen Schepens
 */
@Singleton
@Startup
public class RequestSender {

	private final static Logger LOGGER = Logger.getLogger(RequestSender.class
			.getName());

	@Resource(lookup = "java:/ConnectionFactory")
	ConnectionFactory cf;

	@Inject
	private NodeService nodeService;

	@Inject
	private RoundService roundService;

	@Inject
	private TimeService timeService;

	@Inject
	private Router router;

	@Inject
	private Event<NewsItem> newsEvent;

	private Connection connection;

	private Session session;

	private Map<Integer, MessageProducer> queues = new HashMap<>();

	private Gson gson = new Gson();

	@PostConstruct
	private void init() {
		Context ctx;
		try {
			// Create context
			ctx = new InitialContext();
			// Create connection
			connection = cf.createConnection();
			// Create session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// Get all nodes
			List<Node> nodes = nodeService.getNodes();
			// Create producer for each destination
			for (Node node : nodes) {
				try {
					Queue queue = (Queue) ctx.lookup(node.getJndi());
					MessageProducer messageProducer = session
							.createProducer(queue);
					queues.put(node.getId(), messageProducer);
					LOGGER.log(INFO, node + " initialized");
				} catch (Exception ex) {
					LOGGER.log(SEVERE,
							node + "failed to initialize: " + ex.getMessage());
				}
			}

		} catch (Exception ex) {
			LOGGER.log(SEVERE, getStackTrace(ex));
		}
	}

	@PreDestroy
	private void destroy() {
		try {
			connection.close();
		} catch (Exception ex) {
			LOGGER.log(SEVERE, getStackTrace(ex));
		}
	}

	/**
	 * Distributes an assignment artifacts to all known nodes.
	 * 
	 * @param filename
	 *            The filename of the assignment artifact
	 * @param bytes
	 *            Byte array with the content of the assignment artifact
	 */
	public void sendArtifact(String filename, byte[] bytes) {
		for (MessageProducer producer : queues.values()) {
			try {
				LOGGER.log(INFO, "Sending " + filename + " to "
						+ producer.getDestination().toString());
				BytesMessage message = session.createBytesMessage();
				message.writeBytes(bytes);
				message.setStringProperty("filename", filename);
				producer.send(message);
			} catch (JMSException jx) {
				LOGGER.log(SEVERE, getStackTrace(jx));
			}
		}
	}

	/**
	 * Sends an EditorRequest for a given user to a Build Node. The Request will
	 * be sent to the Build Node associated with the given user.
	 * <p>
	 * If an Exception occurs before the request is sent to the node, a failed
	 * result will be sent to the client immediately by the Router.
	 * </p>
	 * <p>
	 * If the request is a SUBMIT request, the score will be calculated. If the
	 * result of this SUBMIT request is successful, the score will be persisted.
	 * </p>
	 * <p>
	 * If the current round is frozen, a response will be sent back to the
	 * client immediately stating that the round is frozen.
	 * </p>
	 * <p>
	 * Some requests, like COMPILE, TEST and SUBMIT are expensive requests that
	 * will cost many CPU power. A team can not send multiple expensive requests
	 * at the same time. If a team sends an expensive request, all subsequent
	 * requests that are sent while the first request has no response yet will
	 * not be sent to the node. This times out after 60 seconds if no response
	 * is received.
	 * </p>
	 * 
	 * @param user
	 *            The user that initiated the request.
	 * @param request
	 *            The request.
	 */
	public void sendRequest(User user, EditorRequest request) {
		// Log request + send News Item
		String news = user.getTeamname() + " requested " + request.getType();
		LOGGER.log(INFO, news);
		NewsItem newsItem = new NewsItem(news);
		newsEvent.fire(newsItem);
		// If frozen, fail all requests except GET requests
		if (!request.getType().equals(RequestType.GET)
				&& timeService.isFrozen()) {
			EditorResult result = new EditorResult();
			result.setType(request.getType());
			result.setSuccessful(false);
			result.setConsoleOutput("Round is frozen!");
			router.sendErrorResult(user.getUsername(), result);
			return;
		}

		if (router.isExpensive(request.getType())
				&& !router.canHandleRequest(user.getUsername())) {
			EditorResult result = new EditorResult();
			result.setType(request.getType());
			result.setSuccessful(false);
			result.setConsoleOutput("You already have a request running!");
			router.sendErrorResult(user.getUsername(), result);
			return;
		}

		try {
			String json = gson.toJson(request);
			int node = user.getNode().getId();
			Message message = session.createTextMessage(json);
			message.setStringProperty("team", user.getUsername());
			Round round = roundService.getCurrentRound();
			message.setStringProperty("project", round.getAssignment()
					.getArtifact());
			MessageProducer producer = queues.get(node);
			producer.send(message);
			// If type is SUBMIT, calculate the score
			if (request.getType().equals(RequestType.SUBMIT)) {
				int time = timeService.getRemainingTime();
				int multiplier = round.getMultiplier();
				int points = time * multiplier;
				Score score = new Score();
				score.setRound(round);
				score.setUser(user);
				score.setScore(points);
				router.addPendingScore(message.getJMSMessageID(), score);
				LOGGER.log(INFO,
						"Adding pending score for " + user.getTeamname());
			}
			router.addPendingResult(message.getJMSMessageID(), user.getId());
		} catch (Exception ex) {
			EditorResult result = new EditorResult();
			result.setType(request.getType());
			result.setSuccessful(false);
			result.setConsoleOutput(ExceptionUtils.getRootCauseMessage(ex));
			LOGGER.log(SEVERE, getStackTrace(ex));
			router.sendErrorResult(user.getUsername(), result);
		}
	}

	@Lock(LockType.WRITE)
	public void reinitialize() {
		LOGGER.log(INFO, "Reinitializing messaging system...");
		this.destroy();
		this.init();
	}

	/**
	 * Gets a list with all available nodes. This list contains only the nodes
	 * that are successfully connected.
	 * 
	 * @return List with all nodes
	 */
	public List<Integer> getAvailableNodes() {
		return new ArrayList<>(queues.keySet());
	}
}