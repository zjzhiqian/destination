package com.hzq.mq.service;

/**
 * Created by hzq on 16/12/13.
 */
public interface MessageSchedualService {
    /**
     * 处理完成订单时的 未确认的 accounting消息
     */
    void handleAccountingQueuePreSave();

    /**
     * 处理完成订单时 已确认 未消费的 accounting消息
     */
    void handleAccountingQueueSend();

    /**
     * 处理order已确认 未消费的orderCompletePay消息
     */
    void handleOrderQueue();
}
