package com.hzq.message.dao;

import com.hzq.base.dao.Dao;
import com.hzq.message.entity.Message;

import java.util.List;
import java.util.Map;


public interface MessageMapper extends Dao<Message, Integer> {
    Message getMessageByMessageId(String messageId);

    int updateByPk(Message message);

    int deleteMessageByMessageId(String messageId);

    List<Message> selectMessagesByParam(Map param);
}