package com.hzq.order.service;

import com.hzq.order.entity.OrderParam;

import java.util.Map;

/**
 * Created by hzq on 16/12/11.
 */
public interface OrderService {
    /**
     * 下单支付 支付接口
     */
    Map<String, String> initOrderPay(OrderParam orderParam);
}
