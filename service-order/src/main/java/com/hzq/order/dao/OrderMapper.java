package com.hzq.order.dao;


import com.hzq.base.dao.Dao;
import com.hzq.order.entity.Order;

public interface OrderMapper extends Dao<Order,Integer> {
    Order getOrderByOrderNo(String orderNo);
}