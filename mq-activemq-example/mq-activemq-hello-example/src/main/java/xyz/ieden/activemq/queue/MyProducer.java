package xyz.ieden.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.text.MessageFormat;

/**
 * ActiveMQ
 *
 * @author gavin
 * @version 1.0
 * @date 2019/7/7 15:29
 */
public class MyProducer {
    public static final String DEFAULT_BROKER_URL = "tcp://c-108:61616";
    public static final String QUEUE_NAME = "Queue01";

    public static void main(String[] args) {
        Connection connection = null;
        Session session = null;
        MessageProducer messageProducer = null;
        try {
            // 1. 创建连接工厂
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(DEFAULT_BROKER_URL);
            // 2. 通过连接工厂获取连接
            connection = connectionFactory.createConnection();
            connection.start();

            // 3. 创建会话
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // 4. 创建队列
            Queue queue = session.createQueue(QUEUE_NAME);

            // 5. 消息生产者
            messageProducer = session.createProducer(queue);

            System.out.println("消息发送开始.");
            // 6. 消息生产者发送消息
            int tagIndex = 0;
            for (; ; ) {
                // 7. 创建待发送消息体
                TextMessage textMessage = session.createTextMessage("Msg:" + tagIndex);
                // 8. 发送消息
                messageProducer.send(textMessage);
                System.out.println(MessageFormat.format("发送消息 [{0}]", textMessage.getText()));
                Thread.sleep(5 * 1000);
                tagIndex++;
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (messageProducer != null) {
                try {
                    messageProducer.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
