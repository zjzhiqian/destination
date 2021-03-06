package com.hzq.mq.schedual.job;

import com.alibaba.fastjson.JSON;
import com.hzq.message.entity.Message;
import com.hzq.message.enums.MessageQueueName;
import com.hzq.message.enums.MessageStatus;
import com.hzq.message.service.MessageService;
import com.hzq.mq.schedual.util.JobUtil;
import com.hzq.mq.service.BankMessageService;
import com.hzq.order.entity.OrderNotify;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * Created by hzq on 16/12/22.
 */
public class HandleOrderQueueJob implements Job, InitializingBean {


    private static MessageService messageService;

    private static BankMessageService bankMessageService;

    private static Integer commonMinute;
    private static Scheduler scheduler;
    private static String cronExpression;


    private static final Logger logger = LoggerFactory.getLogger(HandleOrderQueueJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        List<Message> messageList = messageService.getLimitMessageByParam(MessageQueueName.ORDER_NOTIFY.name(), commonMinute, MessageStatus.TO_SEND.getVal(), 100);
        logger.debug("handleOrderQueue,message Size {}", messageList.size());
        messageList.forEach(message -> {
            OrderNotify notifyInfo = JSON.parseObject(message.getMessageBody(), OrderNotify.class);
            bankMessageService.completePay(notifyInfo);
        });

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        JobUtil.startJob("HandleOrderQueueJob", "HandleOrderQueueJobCronTrigger", scheduler, cronExpression,this.getClass());
    }


    public void setCommonMinute(Integer commonMinute) {
        HandleOrderQueueJob.commonMinute = commonMinute;
    }

    public void setScheduler(Scheduler scheduler) {
        HandleOrderQueueJob.scheduler = scheduler;
    }

    public void setCronExpression(String cronExpression) {
        HandleOrderQueueJob.cronExpression = cronExpression;
    }

    public void setMessageService(MessageService messageService) {
        HandleOrderQueueJob.messageService = messageService;
    }

    public void setBankMessageService(BankMessageService bankMessageService) {
        HandleOrderQueueJob.bankMessageService = bankMessageService;
    }
}
