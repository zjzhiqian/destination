package com.hzq.base.job;

import org.mengyun.tcctransaction.SystemException;
import org.mengyun.tcctransaction.Transaction;
import org.mengyun.tcctransaction.TransactionRepository;
import org.mengyun.tcctransaction.api.TransactionStatus;
import org.mengyun.tcctransaction.support.TransactionConfigurator;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by hzq on 16/12/21.
 */
public class TccRecoverScheduledJob implements InitializingBean, Job {

    private static TransactionConfigurator transactionConfigurator;

    private Scheduler scheduler;


    private Logger logger = LoggerFactory.getLogger(TccRecoverScheduledJob.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            JobDetail job = JobBuilder.newJob()
                    .storeDurably(true)
                    .withIdentity("transactionRecoveryJob")
                    .ofType(this.getClass())
                    .build();
            CronTriggerImpl trigger = new CronTriggerImpl();
            trigger.setName("transactionRecoveryCronTrigger");
            trigger.setCronExpression(transactionConfigurator.getRecoverConfig().getCronExpression());
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<Transaction> transactions = loadErrorTransactions();
        recoverErrorTransactions(transactions);
    }

    private List<Transaction> loadErrorTransactions() {
        TransactionRepository transactionRepository = transactionConfigurator.getTransactionRepository();
        long currentTimeInMillis = java.util.Calendar.getInstance().getTimeInMillis();
        List<Transaction> transactions = transactionRepository.findAllUnmodifiedSince(new Date(currentTimeInMillis - transactionConfigurator.getRecoverConfig().getRecoverDuration() * 1000));
        List<Transaction> recoverTransactions = new ArrayList<Transaction>();
        for (Transaction transaction : transactions) {
            int result = transactionRepository.update(transaction);
            if (result > 0) {
                recoverTransactions.add(transaction);
            }
        }
        return recoverTransactions;
    }

    private void recoverErrorTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.getRetriedCount() > transactionConfigurator.getRecoverConfig().getMaxRetryCount()) {
                logger.error(String.format("recover failed with max retry count,will not try again. txid:%s, status:%s,retried count:%d", transaction.getXid(), transaction.getStatus().getId(), transaction.getRetriedCount()));
                continue;
            }
            try {
                transaction.addRetriedCount();
                if (transaction.getStatus().equals(TransactionStatus.CONFIRMING)) {
                    transaction.changeStatus(TransactionStatus.CONFIRMING);
                    transactionConfigurator.getTransactionRepository().update(transaction);
                    transaction.commit();
                } else {
                    transaction.changeStatus(TransactionStatus.CANCELLING);
                    transactionConfigurator.getTransactionRepository().update(transaction);
                    transaction.rollback();
                }
                transactionConfigurator.getTransactionRepository().delete(transaction);
            } catch (Throwable e) {
                logger.warn(String.format("recover failed, txid:%s, status:%s,retried count:%d", transaction.getXid(), transaction.getStatus().getId(), transaction.getRetriedCount()), e);
            }
        }
    }



    @Autowired
    private void setTransactionConfigurator(TransactionConfigurator transactionConfigurator) {
        TccRecoverScheduledJob.transactionConfigurator = transactionConfigurator;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }



}

