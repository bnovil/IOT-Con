package gate.MQUtil;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @Author:lzq
 * @Discription
 * @Date: Created on 2017/6/9.
 * @Modified By:
 */
public class ActiveMQUtil {
    public static void senderMessage(String message) {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageProducer messageProducer;
        //ConnectionFactory
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://localhost:61616");

        try {
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(ServerQueue.Gate_Logic);

            messageProducer = session.createProducer(destination);
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            sendMessage(session, messageProducer, message);
            session.commit();
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (null != connection) {
                    connection.close();
                }
            } catch (JMSException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static void sendMessage(Session session, MessageProducer messageProducer, String messages) throws JMSException {
        TextMessage message = session.createTextMessage(messages);
        messageProducer.send(message);
    }
    public static void main(String [] args){
        ActiveMQUtil activeMQUtil= new ActiveMQUtil();
        activeMQUtil.senderMessage("This is a producer message");
    }
}

