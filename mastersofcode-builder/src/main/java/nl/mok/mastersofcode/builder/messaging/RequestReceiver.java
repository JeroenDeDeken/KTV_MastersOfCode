package nl.mok.mastersofcode.builder.messaging;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import static nl.mok.mastersofcode.shared.util.ExceptionUtils.getStackTrace;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedExecutors;
import javax.enterprise.concurrent.ManagedTaskListener;
import javax.inject.Inject;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import nl.mok.mastersofcode.builder.RequestTask;
import nl.mok.mastersofcode.builder.UploadService;
import nl.mok.mastersofcode.shared.domain.EditorResult;

/**
 * Receives editor requests and processes them.
 * 
 * @author Jeroen Schepens
 */
@Startup
@Singleton
public class RequestReceiver implements MessageListener, ManagedTaskListener {

	private final static Logger LOGGER = Logger.getLogger(RequestReceiver.class
			.getName());

	@Inject
	private ResultSender resultSender;

	@Inject
	private UploadService uploadService;

	@Resource
	private ManagedExecutorService managedExecutorService;

	@Resource(lookup = "java:/ConnectionFactory")
	ConnectionFactory cf;

	@Resource(lookup = "java:jboss/build-request")
	private Queue requestQueue;

	private Connection connection;

	@PostConstruct
	private void init() {
		LOGGER.log(INFO, "Connecting to request queue.");
		try {
			connection = cf.createConnection();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			MessageConsumer consumer = session.createConsumer(requestQueue);
			consumer.setMessageListener(this);
			connection.start();
			LOGGER.log(INFO, "Connected to request queue.");
		} catch (JMSException jx) {
			LOGGER.log(SEVERE, getStackTrace(jx));
		}
	}

	@PreDestroy
	private void destroy() {
		LOGGER.log(INFO, "Disconnecting from request queue.");
		try {
			connection.close();
			LOGGER.log(INFO, "Disconnected from request queue.");
		} catch (JMSException jx) {
			LOGGER.log(SEVERE, getStackTrace(jx));
		}
	}

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			// TextMessage = EditorRequest
			TextMessage textMessage = (TextMessage) message;
			try {
				String messageBody = textMessage.getText();
				String messageId = textMessage.getJMSMessageID();
				String team = message.getStringProperty("team");
				String project = message.getStringProperty("project");
				RequestTask task = new RequestTask(messageBody, messageId,
						team, project);
				Callable<EditorResult> managedTask = ManagedExecutors
						.managedTask(task, this);
				managedExecutorService.submit(managedTask);
			} catch (JMSException jx) {
				LOGGER.log(SEVERE, getStackTrace(jx));
			}
		} else if (message instanceof BytesMessage) {
			// BytesMessage = Artifact
			try {
				BytesMessage bytesMessage = (BytesMessage) message;
				String fileName = message.getStringProperty("filename");
				byte[] bytes = new byte[(int) bytesMessage.getBodyLength()];
				bytesMessage.readBytes(bytes);
				uploadService.uploadArtifact(fileName, bytes);
			} catch (JMSException jx) {
				LOGGER.log(SEVERE, getStackTrace(jx));
			}
		} else {
			LOGGER.log(WARNING, "Received incompatible message.");
		}
	}

	@Override
	public void taskAborted(Future<?> arg0, ManagedExecutorService arg1,
			Object arg2, Throwable arg3) {
		LOGGER.log(SEVERE, getStackTrace(arg3));
	}

	@Override
	public void taskDone(Future<?> arg0, ManagedExecutorService arg1,
			Object arg2, Throwable arg3) {
		try {
			EditorResult editorResult = (EditorResult) arg0.get();
			resultSender.sendMessage(editorResult);
		} catch (InterruptedException | ExecutionException ex) {
			LOGGER.log(SEVERE, getStackTrace(ex));
		}
	}

	@Override
	public void taskStarting(Future<?> arg0, ManagedExecutorService arg1,
			Object arg2) {
		return;
	}

	@Override
	public void taskSubmitted(Future<?> arg0, ManagedExecutorService arg1,
			Object arg2) {
		return;
	}
}