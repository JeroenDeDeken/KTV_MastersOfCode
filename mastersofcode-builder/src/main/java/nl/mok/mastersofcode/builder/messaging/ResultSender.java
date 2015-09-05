package nl.mok.mastersofcode.builder.messaging;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static nl.mok.mastersofcode.shared.util.ExceptionUtils.getStackTrace;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import com.google.gson.Gson;

import nl.mok.mastersofcode.shared.domain.EditorResult;

/**
 * Sends results from processed request back to the build reply queue.
 * 
 * @author Jeroen Schepens
 */
@Singleton
public class ResultSender {

	private final static Logger LOGGER = Logger.getLogger(ResultSender.class
			.getName());

	private Gson gson = new Gson();

	@Resource(lookup = "java:/ConnectionFactory")
	ConnectionFactory cf;

	@Resource(lookup = "java:jboss/build-reply")
	private Queue replyQueue;

	private Connection connection;

	private Session session;

	private MessageProducer messageProducer;

	@PostConstruct
	private void init() {
		LOGGER.log(INFO, "Connecting to result queue.");
		try {
			connection = cf.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			messageProducer = session.createProducer(replyQueue);
			connection.start();
			LOGGER.log(INFO, "Connected to request queue.");
		} catch (JMSException jx) {
			LOGGER.log(SEVERE, getStackTrace(jx));
		}
	}

	@PreDestroy
	private void destroy() {
		LOGGER.log(INFO, "Disconnecting from result queue.");
		try {
			connection.close();
			LOGGER.log(INFO, "Disconnected from result queue.");
		} catch (JMSException jx) {
			LOGGER.log(SEVERE, getStackTrace(jx));
		}
	}

	/**
	 * Sends an editor result back to the build reply queue.
	 * 
	 * @param result
	 *            The result to send back
	 */
	public void sendMessage(EditorResult result) {
		String json = gson.toJson(result);
		try {
			Message message = session.createTextMessage(json);
			message.setJMSCorrelationID(result.getId());
			messageProducer.send(message);
		} catch (JMSException jx) {
			LOGGER.log(SEVERE, getStackTrace(jx));
		}
	}
}