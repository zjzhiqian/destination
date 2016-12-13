package com.hzq.mq.listener;

import com.alibaba.fastjson.JSON;
import com.hzq.accounting.entity.AccountingMessage;
import com.hzq.mq.service.AccountMessageService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.Session;

@Component
public class AccountingMessageListener implements SessionAwareMessageListener<Message> {

    private static final Logger logger = LoggerFactory.getLogger(AccountingMessageListener.class);

    @Autowired
    AccountMessageService accountMessageService;



    @JmsListener(id = "accountingMessageListener", destination = "ACCOUNT_NOTIFY", concurrency = "5")
    public void onMessage(Message message, Session session) {
        try {
            ActiveMQTextMessage objectMessage = (ActiveMQTextMessage) message;
            String strMessage = objectMessage.getText();
            AccountingMessage accountingMessage = JSON.parseObject(strMessage, AccountingMessage.class);
            accountMessageService.completeAccounting(accountingMessage);
        } catch (Exception e) {
            logger.error("BankMessageListener receive message error", e);
        }
    }
}
