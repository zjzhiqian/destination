package com.hzq.test.base.mq;


import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Created by hzq on 16/12/8.
 */
public class ActiveMqSender {


    public static void main(String[] args) {
        //连接工厂
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "failover://tcp://192.168.29.101:61616");

        Connection connection = null;
        try {
            //通过连接工厂获取连接
            connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建session
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            //创建一个名称为HelloWorld的消息队列
            Destination destination = session.createQueue("ORDER_NOTIFY");
            //创建消息生产者
            MessageProducer messageProducer = session.createProducer(destination);
            //发送消息
            sendMessage(session, messageProducer);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**61
     * 发送消息
     *
     * @param session         session
     * @param messageProducer 消息生产者
     * @throws Exception
     */
    private static void sendMessage(Session session, MessageProducer messageProducer) throws Exception {
        for (int i = 0; i < 10; i++) {
            //创建一条文本消息
            TextMessage message = session.createTextMessage("ActiveMQ 发送消息" + i);
            System.out.println(1);
            //通过消息生产者发出消息
            messageProducer.send(message);
        }

    }


}
