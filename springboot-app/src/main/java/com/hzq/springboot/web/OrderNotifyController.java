package com.hzq.springboot.web;

//import com.alibaba.fastjson.JSON;
//import com.hzq.base.util.UUIDUtils;
//import com.hzq.message.entity.Message;
//import com.hzq.message.enums.MessageQueueName;
//import com.hzq.message.service.MessageService;
//import com.hzq.order.entity.OrderNotify;
//import com.hzq.order.service.OrderService;
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


//    @Autowired
//    OrderService orderService;
//
//    @Autowired
//    MessageService messageService;
//
//    @PostMapping("notify")
//    public String orderNotify(OrderNotify orderNotify) {
//        String messageId = UUIDUtils.get32UUID();
//        Message message = new Message(messageId, MessageQueueName.ORDER_NOTIFY.toString(), JSON.toJSONString(orderNotify));
//        int result = messageService.saveAndSendMessage(message);
//        if (result > 0) {
//
//        }
//
//        System.out.println(result);
//        return "回调成功";
//    }
}
