package com.hzq.springboot.web;

import com.hzq.order.entity.OrderParam;
import com.hzq.order.service.OrderService;

import com.hzq.springboot.OO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by hzq on 16/12/11.
 */

@RestController
@RequestMapping("orders")
public class OrderController {


    @Autowired
    OrderService orderService;

    @PostMapping("pay")
    public Map<String, String> orderPay(OrderParam orderParam) {
        Map<String, String> resultMap = orderService.initOrderPay(orderParam);
        System.out.println(resultMap);
        return resultMap;

    }
}
