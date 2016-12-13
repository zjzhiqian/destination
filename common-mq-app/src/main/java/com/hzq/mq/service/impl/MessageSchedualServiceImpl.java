package com.hzq.mq.service.impl;

import com.hzq.accounting.entity.Accounting;
import com.hzq.accounting.service.AccountingService;
import com.hzq.message.entity.Message;
import com.hzq.message.enums.MessageQueueName;
import com.hzq.message.enums.MessageStatus;
import com.hzq.message.service.MessageService;
import com.hzq.mq.service.MessageSchedualService;
import com.hzq.order.entity.OrderRecord;
import com.hzq.order.enums.OrderStatusEnume;
import com.hzq.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时处理异常消息
 * Created by hzq on 16/12/13.
 */
@Service
public class MessageSchedualServiceImpl implements MessageSchedualService {

    @Autowired
    MessageService messageService;

    @Autowired
    OrderService orderService;

    @Autowired
    AccountingService accountingService;


    @Override
    public void handleWaitingConfirmTimeOutOrderCompleteQueue() {
        List<Message> messageList = messageService.getLimitMessageByParam(MessageQueueName.ACCOUNT_NOTIFY.name(), 5, MessageStatus.PRE_CONFIRM.getVal(), 100);
        messageList.forEach(message -> {
            String bankOrderNo = message.getField1();
            OrderRecord orderRecord = orderService.getOrderRecordByBankNo(bankOrderNo);
            if (OrderStatusEnume.PAY_SUCCESS.getVal().equals(orderRecord.getStatus())) {
                // 确认并发送消息
                messageService.confirmAndSendMessage(message.getMessageId());
            } else if (OrderStatusEnume.WAIT_PAY.getVal().equals(orderRecord.getStatus())) {
                // 订单状态是等到支付，可以直接删除数据
                messageService.deleteMessageByMessageId(message.getMessageId());
            }
        });
    }

    @Override
    public void handleSendingTimeOutOrderCompleteQueue() {
        List<Message> messageList = messageService.getLimitMessageByParam(MessageQueueName.ACCOUNT_NOTIFY.name(), 5, MessageStatus.TO_SEND.getVal(), 100);
        messageList.forEach(message -> {
            String messageId = message.getMessageId();
            String bankOrderNo = message.getField1();

            if (message.getMessageSendTimes() >= 5) {
                messageService.setMessageDead(messageId);
            } else {
                //可以不用作判断,消费端必须作幂等!
                Accounting accounting = accountingService.getAccountingByBankOrderNo(bankOrderNo);
                if (accounting == null) {
                    messageService.reSendMessageByMessageId(messageId);
                } else {
                    messageService.deleteMessageByMessageId(messageId);
                }
            }
        });
    }
}
