package nl.mok.mastersofcode.service.messaging;

import static java.util.logging.Level.INFO;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import nl.mok.mastersofcode.service.core.Router;

/**
 * Message driven bean that receives build replies processed by the build
 * servers. It passes the received messages to the router, which sends the
 * results back to the requesting clients.
 * 
 * @author Jeroen Schepens
 *
 */
@MessageDriven(name = "ResultReceiver", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "buildReply"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class ResultReceiver implements MessageListener {

	private final static Logger LOGGER = Logger
			.getLogger("nl.mok.mastersofcode.service");

	@Inject
	private Router router;

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			String messageBody = textMessage.getText();
			String correlationId = textMessage.getJMSCorrelationID();
			router.processResult(correlationId, messageBody);
		} catch (Exception ex) {
			LOGGER.log(INFO, ex.getMessage());
		}
	}
}