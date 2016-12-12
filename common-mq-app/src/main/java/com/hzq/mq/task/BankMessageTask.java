package com.hzq.mq.task;

import com.hzq.mq.service.BankMessageService;
import com.hzq.order.entity.OrderNotify;

/**
 * Created by hzq on 16/12/12.
 */
public class BankMessageTask implements Runnable {

    private OrderNotify orderNotify;

    private BankMessageService bankMessageService;

    public BankMessageTask(OrderNotify orderNotify, BankMessageService bankMessageService) {
        this.orderNotify = orderNotify;
        this.bankMessageService = bankMessageService;
    }


    @Override
    public void run() {
    }
}
