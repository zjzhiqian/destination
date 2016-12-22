package com.hzq.mq.schedual.job;

import com.hzq.accounting.entity.Accounting;
import com.hzq.accounting.service.AccountingService;
import com.hzq.message.entity.Message;
import com.hzq.message.enums.MessageQueueName;
import com.hzq.message.enums.MessageStatus;
import com.hzq.message.service.MessageService;
import org.mengyun.tcctransaction.SystemException;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * Created by hzq on 16/12/22.
 */
public class HandleAccountingQueueSendJob implements Job, InitializingBean {


    private static MessageService messageService;
    private static AccountingService accountingService;
    private static Integer commonMinute;
    private static Scheduler scheduler;
    private static String cronExpression;


    private static final Logger logger = LoggerFactory.getLogger(HandleAccountingQueuePreSaveJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        List<Message> messageList = messageService.getLimitMessageByParam(MessageQueueName.ACCOUNT_NOTIFY.name(), commonMinute, MessageStatus.TO_SEND.getVal(), 100);
        logger.warn("handleAccountingQueueSend,message Size {}", messageList.size());
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


    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            JobDetail job = JobBuilder.newJob()
                    .storeDurably(true)
                    .withIdentity("HandleAccountingQueueSendJob")
                    .ofType(this.getClass())
                    .build();
            CronTriggerImpl trigger = new CronTriggerImpl();
            trigger.setName("HandleAccountingQueueSendJobCronTrigger");
            trigger.setCronExpression(cronExpression);
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }


    public void setCommonMinute(Integer commonMinute) {
        HandleAccountingQueueSendJob.commonMinute = commonMinute;
    }

    public void setScheduler(Scheduler scheduler) {
        HandleAccountingQueueSendJob.scheduler = scheduler;
    }

    public void setCronExpression(String cronExpression) {
        HandleAccountingQueueSendJob.cronExpression = cronExpression;
    }

    public  void setMessageService(MessageService messageService) {
        HandleAccountingQueueSendJob.messageService = messageService;
    }

    public  void setAccountingService(AccountingService accountingService) {
        HandleAccountingQueueSendJob.accountingService = accountingService;
    }
}
