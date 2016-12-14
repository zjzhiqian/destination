package com.hzq.mq.schedual;

import com.hzq.mq.service.MessageSchedualService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by hzq on 16/12/13.
 */
@Component
public class MessageSchedualTask implements InitializingBean {

    @Autowired
    MessageSchedualService messageSchedualService;

    private static final ThreadPoolExecutor executors = new ThreadPoolExecutor(5, 20, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());


    @Override
    public void afterPropertiesSet() throws Exception {
        // 开一个子线程处理 处理待确认,超时的(完成订单逻辑)的消息
        Runnable handleAccountingQueuePreSave = messageSchedualService::handleAccountingQueuePreSave;
        Runnable handleAccountingQueueSend = messageSchedualService::handleAccountingQueueSend;
        Runnable handleOrderQueue = messageSchedualService::handleOrderQueue;
        executors.execute(new RunnableTask(handleAccountingQueuePreSave, 20));
        executors.execute(new RunnableTask(handleAccountingQueueSend, 20));
        executors.execute(new RunnableTask(handleOrderQueue, 20));
    }

    private static class RunnableTask implements Runnable {
        private final Runnable runnable;
        private final int sleepSecond;

        RunnableTask(Runnable runnable, int sleepSecond) {
            this.runnable = runnable;
            this.sleepSecond = sleepSecond;
        }

        @Override
        public void run() {
            while (true) {
                runnable.run();
                try {
                    TimeUnit.SECONDS.sleep(sleepSecond);
                } catch (InterruptedException ignored) {
                }
            }

        }
    }

}
