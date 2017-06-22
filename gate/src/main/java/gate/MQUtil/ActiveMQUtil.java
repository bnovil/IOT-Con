package gate.MQUtil;

import com.google.protobuf.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;
import protobuf.generate.device.Device;

import javax.jms.*;

/**
 * @Author:lzq
 * @Discription
 * @Date: Created on 2017/6/9.
 * @Modified By:
 */
public class ActiveMQUtil {
    public static void sendMessage(byte[] message) {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageProducer messageProducer;
        //ConnectionFactory
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD,
                ServerConstants.BROKER_URL);

        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(ServerConstants.Gate_Logic);

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

    public static void sendMessage(Session session, MessageProducer messageProducer, byte[] messages) throws JMSException {
//        Message message = session.createMessage(messages);
        ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) session.createBytesMessage();
        bytesMessage.writeBytes(messages);
        messageProducer.send(bytesMessage);
    }

    public static void main(String [] args){
        ActiveMQUtil activeMQUtil= new ActiveMQUtil();
//        for (int i = 0; i < 10; i++) {
//            activeMQUtil.senderMessage(("This is a producer message " + i).getBytes());
//        }
        Device.DeviceMessage.Builder deviceMessage = Device.DeviceMessage.newBuilder();
//        sp.setContent(msg.getContent());
        deviceMessage.setContent("gate send to logic ");
        deviceMessage.setSelf("2");
        deviceMessage.setDest("3");

        byte[] m = deviceMessage.build().toByteArray();
        sendMessage(m);

    }
}

