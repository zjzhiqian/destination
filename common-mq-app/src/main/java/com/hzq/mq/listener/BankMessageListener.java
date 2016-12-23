package com.hzq.mq.listener;

import com.alibaba.fastjson.JSON;
import com.hzq.mq.service.BankMessageService;
import com.hzq.order.entity.OrderNotify;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.Session;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 监听银行回调消息
 */
@Component
public class BankMessageListener implements SessionAwareMessageListener<Message> {

    private static final Logger logger = LoggerFactory.getLogger(BankMessageListener.class);

    @Autowired
    private BankMessageService bankMessageService;

    private final static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @JmsListener(id = "bankMessageListener", destination = "ORDER_NOTIFY", concurrency = "5")
    public void onMessage(Message message, Session session) {
        try {
            ActiveMQTextMessage objectMessage = (ActiveMQTextMessage) message;
            String strMessage = objectMessage.getText();
            OrderNotify notifyInfo = JSON.parseObject(strMessage, OrderNotify.class);
            fixedThreadPool.execute(() -> bankMessageService.completePay(notifyInfo));   //TODO 这里线程池抛出异常的默认处理策略是什么呢?
        } catch (Exception e) {
            logger.error("BankMessageListener receive message error", e);
        }
    }
}
