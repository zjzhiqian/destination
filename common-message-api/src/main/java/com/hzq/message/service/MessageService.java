package com.hzq.message.service;

import com.hzq.message.entity.Message;
import com.hzq.message.exception.MessageBizException;

/**
 * 消息服务API
 * Created by hzq on 16/12/8.
 */
public interface MessageService {

    /**
     * 预存储消息.
     */
    public int preSaveMessage(Message message) throws MessageBizException;


    /**
     * 确认并发送消息.
     */
    public int confirmMessage(String messageId) throws MessageBizException;


    /**
     * 存储并发送消息.
     */
    public int saveAndSendMessage(Message message) throws MessageBizException;


    /**
     * 直接发送消息.
     */
    public int directSendMessage(Message message) throws MessageBizException;


    /**
     * 重发消息.
     */
    public int reSendMessage(Message message) throws MessageBizException;


    /**
     * 根据messageId重发某条消息.
     */
    public int reSendMessageByMessageId(String messageId) throws MessageBizException;


    /**
     * 将消息标记为死亡消息.
     */
    public int setMessageToAreadlyDead(String messageId) throws MessageBizException;


    /**
     * 根据消息ID获取消息
     */
    public Message getMessageByMessageId(String messageId) throws MessageBizException;

    /**
     * 根据消息ID删除消息
     */
    public int deleteMessageByMessageId(String messageId) throws MessageBizException;


    /**
     * 重发某个消息队列中的全部已死亡的消息.
     */
    public void reSendAllDeadMessageByQueueName(String queueName, int batchSize) throws MessageBizException;

}
