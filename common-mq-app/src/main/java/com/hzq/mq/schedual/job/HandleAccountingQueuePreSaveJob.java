package com.hzq.mq.schedual.job;

/**
 * Created by hzq on 16/12/22.
 */

import com.hzq.message.entity.Message;
import com.hzq.message.enums.MessageQueueName;
import com.hzq.message.enums.MessageStatus;
import com.hzq.message.service.MessageService;
import com.hzq.mq.schedual.util.JobUtil;
import com.hzq.order.entity.OrderRecord;
import com.hzq.order.enums.OrderStatusEnume;
import com.hzq.order.service.OrderService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * Created by hzq on 16/12/22.
 */
public class HandleAccountingQueuePreSaveJob implements Job, InitializingBean {


    private static MessageService messageService;
    private static OrderService orderService;
    private static Integer commonMinute;
    private static Scheduler scheduler;
    private static String cronExpression;


    private static final Logger logger = LoggerFactory.getLogger(HandleAccountingQueuePreSaveJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<Message> messageList = messageService.getLimitMessageByParam(MessageQueueName.ACCOUNT_NOTIFY.name(), commonMinute, MessageStatus.PRE_CONFIRM.getVal(), 100);
        logger.warn("handleAccountingQueuePreSave,message Size {}", messageList.size());
        messageList.forEach(message -> {
            String bankOrderNo = message.getField1();
            OrderRecord orderRecord = orderService.getOrderRecordByBankNo(bankOrderNo);
            if (OrderStatusEnume.PAY_SUCCESS.getVal().equals(orderRecord.getStatus())) {
                // 确认并发送消息
                messageService.confirmAndSendMessage(message.getMessageId());
            } else if (OrderStatusEnume.WAIT_PAY.getVal().equals(orderRecord.getStatus()) || OrderStatusEnume.PAY_FAIL.getVal().equals(orderRecord.getStatus())) {
                // 订单状态是等待支付或者支付失败，可以直接删除数据
                messageService.deleteMessageByMessageId(message.getMessageId());
            }
        });
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        JobUtil.startJob("HandleAccountingQueuePreSaveJob","HandleAccountingQueuePreSaveJobCronTrigger",scheduler,cronExpression,this.getClass());
    }




    public void setCommonMinute(Integer commonMinute) {
        HandleAccountingQueuePreSaveJob.commonMinute = commonMinute;
    }

    public void setScheduler(Scheduler scheduler) {
        HandleAccountingQueuePreSaveJob.scheduler = scheduler;
    }

    public void setCronExpression(String cronExpression) {
        HandleAccountingQueuePreSaveJob.cronExpression = cronExpression;
    }

    public void setMessageService(MessageService messageService) {
        HandleAccountingQueuePreSaveJob.messageService = messageService;
    }

    public void setOrderService(OrderService orderService) {
        HandleAccountingQueuePreSaveJob.orderService = orderService;
    }

}

