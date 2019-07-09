package xyz.ieden.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author gavin
 * @version 1.0
 * @date 2019/7/7 20:50
 */
public class MyConsumer {

    public static final String DEFAULT_BROKER_URL = "tcp://c-108:61616";
    public static final String QUEUE_NAME = "Queue01";

    public static void main(String[] args) {
        Connection connection = null;
        Session session = null;
        MessageConsumer messageConsumer = null;
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

            // 5. 消息消费者
            messageConsumer = session.createConsumer(queue);

            System.out.println("消息接收开始.");
            // 6. 消息生产者发送消息
            for (; ; ) {
                // 8. 发送消息
                TextMessage textMessage = (TextMessage) messageConsumer.receive();
                System.out.println("接收到的消息：" + textMessage.getText());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (messageConsumer != null) {
                try {
                    messageConsumer.close();
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
