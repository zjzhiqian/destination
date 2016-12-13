package com.hzq.mq.service.impl;

import com.hzq.message.service.MessageService;
import com.hzq.mq.service.BankMessageService;
import com.hzq.order.entity.OrderNotify;
import com.hzq.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hzq on 16/12/13.
 */
@Service
public class BankMessageServiceImpl implements BankMessageService {

    private static final Logger logger = LoggerFactory.getLogger(BankMessageServiceImpl.class);

    @Autowired
    OrderService orderService;

    @Autowired
    MessageService messageService;


    @Override
    public void completePay(OrderNotify orderNotify) {
        try {
            orderService.completePay(orderNotify);  //完成交易
            messageService.deleteMessageByMessageId(orderNotify.getMessageId()); //删除消息
        } catch (Exception e) {
            logger.error("完成支付结果异常:", e);
        }
    }
}
