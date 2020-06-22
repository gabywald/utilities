package gabywald.global.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Java Message Service (JMS) Sender example. 
 * @author Gabriel Chandesris (2020)
 */
public class MessageSender {

	private final MessageProducer producer;
	private final Session session;
	private final Connection con;

	public MessageSender(ConnectionFactory factory) throws JMSException {
		this.con					= factory.createConnection();
		this.con.start();
		this.session				= this.con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue					= this.session.createQueue("example.queue");
		this.producer				= this.session.createProducer(queue);
	}

	public void sendMessage(String message) throws JMSException {
		System.out.printf(	"Sending message: %s, Thread:%s%n",
							message, Thread.currentThread().getName());
		TextMessage textMessage		= this.session.createTextMessage(message);
		this.producer.send(textMessage);
	}

	public void destroy() throws JMSException {
		this.con.close();
	}
}