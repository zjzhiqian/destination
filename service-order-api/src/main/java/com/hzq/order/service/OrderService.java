package com.hzq.order.service;

import com.hzq.order.entity.OrderNotify;
import com.hzq.order.entity.OrderParam;

import java.util.Map;

/**
 * Created by hzq on 16/12/11.
 */
public interface OrderService {
    /**
     * 下单支付 支付接口
     */
    String initOrderPay(OrderParam orderParam);

    /**
     * 支付回调
     */
    void completePay(OrderNotify orderNotify);
}
