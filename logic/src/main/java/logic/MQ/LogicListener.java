package logic.MQ;

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
            consumer.setMessageListener(new MessageListener() {
//                                            @Override
//                                            public void onMessage(Message m) {
//                                                TextMessage textMsg = (TextMessage) m;
//                                                try {
//                                                    System.out.println(textMsg.getText());
//                                                } catch (JMSException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
                @Override
                public void onMessage(Message m) {
                    if(m instanceof ActiveMQBytesMessage){
                        ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) m;
//                        byte[] temp =  bytesMessage.readBytes();
//
//                        Device.DeviceMessage.parseFrom(bytesMessage)
                    }

                }
                                        }
            );
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
}
