package com.hzq.mq.schedual;

import com.hzq.mq.service.MessageSchedualService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * Created by hzq on 16/12/13.
 */
@Component
public class MessageSchedualTask implements InitializingBean {

    @Autowired
    MessageSchedualService messageSchedualService;

    ScheduledExecutorService executors = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public void afterPropertiesSet() throws Exception {
        // 开一个子线程处理 处理待确认,超时的(完成订单逻辑)的消息
        executors.scheduleAtFixedRate(messageSchedualService::handleAccountingQueuePreSave, 20, 20, TimeUnit.SECONDS);
        executors.scheduleAtFixedRate(messageSchedualService::handleAccountingQueueSend, 20, 20, TimeUnit.SECONDS);
        executors.scheduleAtFixedRate(messageSchedualService::handleOrderQueue, 20, 20, TimeUnit.SECONDS);
    }
}
