package com.hzq.message.service;

import com.hzq.message.entity.Message;
import com.hzq.message.exception.MessageException;

import java.util.List;

/**
 * 消息服务API
 * Created by hzq on 16/12/8.
 */
public interface MessageService {

    /**
     * 预存储消息.
     */
    int preSaveMessage(Message message) throws MessageException;


    /**
     * 确认并发送消息.
     */
    int confirmAndSendMessage(String messageId) throws MessageException;


    /**
     * 存储并发送消息到mq.
     */
    int saveAndSendMessage(Message message) throws MessageException;


    /**
     * 直接发送消息到mq.
     */
    void directSendMessage(Message message) throws MessageException;

    /**
     * 根据messageId重发某条消息.(未死亡的消息)
     */
    int reSendMessageByMessageId(String messageId) throws MessageException;


    /**
     * 将消息标记为死亡消息.
     */
    int setMessageDead(String messageId) throws MessageException;


    /**
     * 根据消息ID获取消息
     */
    Message getMessageByMessageId(String messageId) throws MessageException;

    /**
     * 根据消息ID删除消息
     */
    int deleteMessageByMessageId(String messageId) throws MessageException;


    /**
     * 重发某个消息队列中的全部已死亡的消息.
     */
    void resendDeadMessageByQueue(String queueName) throws MessageException;

    /**
     * 按条件批量获取消息
     * @param queueName 队列名称
     * @param times 超时时间
     * @param status 状态
     * @param count 查询数量
     * @return
     */
    List<Message> getLimitMessageByParam(String queueName, int times, int status, int count);


}
