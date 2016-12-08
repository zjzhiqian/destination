package com.hzq.message.service.impl;

import com.hzq.message.dao.MessageMapper;
import com.hzq.message.entity.Message;
import com.hzq.message.enums.MessageStatus;
import com.hzq.message.exception.MessageBizException;
import com.hzq.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * MessageServiceImpl
 * Created by hzq on 16/12/8.
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Override
    public int preSaveMessage(Message message) throws MessageBizException {
        message.setStatus(MessageStatus.PRE_CONFIRM.getVal());
        return messageMapper.insert(message);
    }

    @Override
    public int confirmMessage(String messageId) throws MessageBizException {
        return 0;
    }

    @Override
    public int saveAndSendMessage(Message message) throws MessageBizException {
        return 0;
    }

    @Override
    public int directSendMessage(Message message) throws MessageBizException {
        return 0;
    }

    @Override
    public int reSendMessage(Message message) throws MessageBizException {
        return 0;
    }

    @Override
    public int reSendMessageByMessageId(String messageId) throws MessageBizException {
        return 0;
    }

    @Override
    public int setMessageToAreadlyDead(String messageId) throws MessageBizException {
        return 0;
    }

    @Override
    public Message getMessageByMessageId(String messageId) throws MessageBizException {
        return null;
    }

    @Override
    public int deleteMessageByMessageId(String messageId) throws MessageBizException {
        return 0;
    }

    @Override
    public void reSendAllDeadMessageByQueueName(String queueName, int batchSize) throws MessageBizException {

    }
}
