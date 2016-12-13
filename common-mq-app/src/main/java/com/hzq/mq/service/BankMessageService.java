package com.hzq.mq.service;

import com.hzq.order.entity.OrderNotify;
import org.springframework.stereotype.Service;

/**
 * Created by hzq on 16/12/12.
 */
@Service
public interface BankMessageService {

    void completePay(OrderNotify orderNotify);
}
