/*
 * ====================================================================
 * 龙果学院： www.roncoo.com （微信公众号：RonCoo_com）
 * 超级教程系列：《微服务架构的分布式事务解决方案》视频教程
 * 讲师：吴水成（水到渠成），840765167@qq.com
 * 课程地址：http://www.roncoo.com/course/view/7ae3d7eddc4742f78b0548aa8bd9ccdb
 * ====================================================================
 */
package com.hzq.mq.listener;

import com.alibaba.fastjson.JSON;
import com.hzq.message.enums.MessageQueueName;
import com.hzq.mq.service.BankMessageService;
import com.hzq.order.entity.OrderNotify;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.Session;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class BankMessageListener implements SessionAwareMessageListener<Message> {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BankMessageListener.class);

    @Autowired
    private BankMessageService bankMessageService;

    private final static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    @JmsListener(id = "BankMessageListener", destination = "ORDER_NOTIFY", concurrency = "2")
    public void onMessage(Message message, Session session) {
        try {
            ActiveMQTextMessage objectMessage = (ActiveMQTextMessage) message;
            String strMessage = objectMessage.getText();
            OrderNotify notifyInfo = JSON.parseObject(strMessage, OrderNotify.class);
            fixedThreadPool.execute(() -> bankMessageService.completePay(notifyInfo));
        } catch (Exception e) {
            logger.error("BankMessageListener receive message error", e);
        }
    }
}
