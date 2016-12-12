package com.hzq.springboot.web;

import com.hzq.order.entity.OrderParam;
import com.hzq.order.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by hzq on 16/12/11.
 */
@RestController
@RequestMapping("orders")
public class OrderController {


    @Autowired
    OrderService orderService;

    @PostMapping("pay")
    public String orderPay(OrderParam orderParam) {
        String result = orderService.initOrderPay(orderParam);
        System.out.println(result);
        return result;

    }
}
