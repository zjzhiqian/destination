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

    private static final Logger logger = LoggerFactory.getLogger(MessageSchedualTask.class);


    @Override
    public void afterPropertiesSet() throws Exception {
        // 开一个子线程处理 处理待确认,超时的(完成订单逻辑)的消息
        executors.execute(() -> {
            while (true) {
                messageSchedualService.handleWaitingConfirmTimeOutOrderCompleteQueue();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ignored) {
                }
            }
        });

        // 开一个子线程处理状态为“已确认” 但已超时的消息未删除.
        executors.execute(() -> {
            while (true) {
                messageSchedualService.handleSendingTimeOutOrderCompleteQueue();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ignored) {
                }
            }
        });
    }
}
