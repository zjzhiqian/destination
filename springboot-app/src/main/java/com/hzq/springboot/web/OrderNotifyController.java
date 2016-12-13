package com.hzq.springboot.web;

import com.alibaba.fastjson.JSON;
import com.hzq.base.util.UUIDUtils;
import com.hzq.message.entity.Message;
import com.hzq.message.enums.MessageQueueName;
import com.hzq.message.service.MessageService;
import com.hzq.order.entity.OrderNotify;
import com.hzq.order.service.OrderService;
import com.hzq.springboot.entity.RawString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by hzq on 16/12/11.
 */
@RestController
@RequestMapping("orderNotify")
public class OrderNotifyController {


    @Autowired
    OrderService orderService;

    @Autowired
    MessageService messageService;

    @PostMapping("notify")
    public RawString orderNotify(OrderNotify orderNotify) {
        //订单回调 不做校验
        String messageId = UUIDUtils.get32UUID();
        orderNotify.setMessageId(messageId);
        Message message = new Message(messageId, MessageQueueName.ORDER_NOTIFY.toString(), JSON.toJSONString(orderNotify));
        int result = messageService.saveAndSendMessage(message);
        //通知商户
        if (result > 0) {
//            商户url通知消息实体
            String merchantNotifyUrl = "notifyUrl";
//            String merchantNotifyUrl = rpTradePaymentManagerService.getMerchantNotifyMessage(payWayCode, notifyMap);
            String notifyMessageId = UUIDUtils.get32UUID();
            Message merchantMessage = new Message(notifyMessageId, MessageQueueName.MERCHANT_NOTIFY.toString(), merchantNotifyUrl);
            messageService.directSendMessage(merchantMessage);
        }
        return new RawString("回调成功");
    }
}
