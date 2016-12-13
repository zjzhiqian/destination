package com.hzq.message.service.impl;

import com.google.common.collect.ImmutableMap;
import com.hzq.message.dao.MessageMapper;
import com.hzq.message.entity.Message;
import com.hzq.message.enums.MessageStatus;
import com.hzq.message.exception.MessageException;
import com.hzq.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * MessageServiceImpl
 * Created by hzq on 16/12/8.
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageMapper messageMapper;


    @Autowired
    private JmsTemplate messageJmsTemplate;


    private void sendMessageToMq(Message message) {
        messageJmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
        messageJmsTemplate.send(session -> session.createTextMessage(message.getMessageBody()));
    }

    private void commonValidMessage(Message message) {
        if (message == null)
            throw new MessageException("保存的消息为空");
        if (StringUtils.isEmpty(message.getConsumerQueue()))
            throw new MessageException("消息的消费队列不能为空");
    }


    @Override
    public int preSaveMessage(Message message) throws MessageException {
        commonValidMessage(message);
        message.setStatus(MessageStatus.PRE_CONFIRM.getVal());
        return messageMapper.insert(message);
    }

    @Override
    public int confirmAndSendMessage(String messageId) throws MessageException {
        Message message = Optional.ofNullable(getMessageByMessageId(messageId)).orElseThrow(() -> new MessageException("消息不存在"));
        int result = 0;
        if (message.getStatus().equals(MessageStatus.PRE_CONFIRM.getVal())) {
            message.setStatus(MessageStatus.TO_SEND.getVal());
            result = messageMapper.updateByPk(message);
            sendMessageToMq(message);
        }
        return result;
    }

    @Override
    public int saveAndSendMessage(Message message) throws MessageException {
        commonValidMessage(message);
        message.setStatus(MessageStatus.TO_SEND.getVal());
        int result = messageMapper.insert(message);
        sendMessageToMq(message);
        return result;
    }

    @Override
    public void directSendMessage(Message message) throws MessageException {
        commonValidMessage(message);
        sendMessageToMq(message);
    }


    @Override
    public int reSendMessageByMessageId(String messageId) throws MessageException {
        Message message = Optional.ofNullable(getMessageByMessageId(messageId)).orElseThrow(() -> new MessageException("消息不存在"));
        int result = 0;
        if (message.getIsDead() != 1) {
            message.setMessageSendTimes(message.getMessageSendTimes() + 1);
            result = messageMapper.updateByPk(message);
            sendMessageToMq(message);
        }
        return result;
    }

    @Override
    public int setMessageDead(String messageId) throws MessageException {
        Message message = Optional.ofNullable(getMessageByMessageId(messageId)).orElseThrow(() -> new MessageException("消息不存在"));
        message.setIsDead(1);
        return messageMapper.updateByPk(message);
    }

    @Override
    public Message getMessageByMessageId(String messageId) throws MessageException {
        return messageMapper.getMessageByMessageId(messageId);
    }

    @Override
    public int deleteMessageByMessageId(String messageId) throws MessageException {
        return messageMapper.deleteMessageByMessageId(messageId);
    }

    @Override
    public void resendDeadMessageByQueue(String queueName) throws MessageException {
        List<Message> messageList = messageMapper.selectMessagesByParam(ImmutableMap.of("queueName", queueName, "idDead", 1));
        messageList.forEach(this::sendMessageToMq);
    }


    @Override
    public List<Message> getLimitMessageByParam(String queueName, int times, int status, int count) {
        return messageMapper.getLimitMessageByParam(queueName,times,status,count);
    }
}
