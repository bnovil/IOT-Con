package logic.MQ;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;
import protobuf.generate.device.Device;

import javax.jms.*;

/**
 * @Author:lzq
 * @Discription
 * @Date: Created on 2017/6/15.
 * @Modified By:
 */
public class LogicListener implements Runnable {
    ConnectionFactory connectionFactory;
    Connection connection = null;
    Session session;
    Destination destination;
    MessageConsumer consumer;

    @Override
    public void run() {
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD, ServerConstants.BROKER_URL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(ServerConstants.Gate_Logic);
            consumer = session.createConsumer(destination);
            consumer.setMessageListener(new GateMessageListner());

        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (null != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        LogicListener gateListener = new LogicListener();
        new Thread(gateListener).start();
    }

    class GateMessageListner implements MessageListener {

        @Override
        public void onMessage(Message m) {
            if (m instanceof ActiveMQBytesMessage) {
                ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) m;
                int len ;
                byte[] temp = null;
                try {
                    len = (int) bytesMessage.getBodyLength();
                    temp = new byte[len];
                    bytesMessage.readBytes(temp);

                } catch (JMSException e) {
                    e.printStackTrace();
                }

                try {
                    Device.DeviceMessage d= Device.DeviceMessage.parseFrom(temp);
                    System.out.println(d.getContent());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
