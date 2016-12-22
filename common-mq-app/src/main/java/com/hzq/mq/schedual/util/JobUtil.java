package com.hzq.mq.schedual.util;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.triggers.CronTriggerImpl;

/**
 * Created by hzq on 16/12/22.
 */
public class JobUtil {
    /**
     * 开始定时任务
     * @param jobName  任务名称
     * @param triggerName 触发器名称
     * @param scheduler scheduler
     * @param cronExpression 表达式
     * @param jobClass 任务类
     */
    public static void startJob(String jobName, String triggerName, Scheduler scheduler, String cronExpression,Class<? extends Job> jobClass){
        try {
            JobDetail job = JobBuilder.newJob()
                    .storeDurably(true)
                    .withIdentity(jobName)
                    .ofType(jobClass)
                    .build();
            CronTriggerImpl trigger = new CronTriggerImpl();
            trigger.setName(triggerName);
            trigger.setCronExpression(cronExpression);
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
