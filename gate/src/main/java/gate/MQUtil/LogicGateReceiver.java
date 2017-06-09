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
public class LogicGateReceiver{
    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageConsumer consumer;
        //ConnectionFactory
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD , "tcp://localhost:61616");
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(ServerQueue.Gate_Logic);
            consumer = session.createConsumer(destination);
            while(true){
                TextMessage message = (TextMessage) consumer.receive(100000);
                if(null != message){
                    System.out.println(message.getText());
                }else{
                    break;
                }
            }
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try{
                if(null != null){
                    connection.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
