package gabywald.global.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Java Message Service (JMS) Receiver example. 
 * @author Gabriel Chandesris (2020)
 */
public class MessageReceiver implements MessageListener {

	private Connection con;

	public void startListener(ConnectionFactory factory) throws JMSException {
		this.con					= factory.createConnection();
		this.con.start();
		Session session				= this.con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue					= session.createQueue("example.queue");
		MessageConsumer consumer	= session.createConsumer(queue);
		consumer.setMessageListener(this);
	}

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			TextMessage tm = (TextMessage) message;
			try {
				System.out.printf("Message received: %s, Thread: %s%n",
						tm.getText(), Thread.currentThread().getName());
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void destroy() throws JMSException {
		this.con.close();
	}
}