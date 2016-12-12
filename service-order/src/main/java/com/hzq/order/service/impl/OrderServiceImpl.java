package com.hzq.order.service.impl;

import com.google.common.collect.ImmutableMap;
import com.hzq.base.util.Creator;
import com.hzq.order.dao.OrderMapper;
import com.hzq.order.dao.OrderRecordMapper;
import com.hzq.order.entity.Order;
import com.hzq.order.entity.OrderParam;
import com.hzq.order.entity.OrderRecord;
import com.hzq.order.enums.OrderStatusEnume;
import com.hzq.order.exception.OrderBizException;
import com.hzq.order.service.OrderService;
import com.hzq.order.util.OrderUtil;
import com.hzq.user.entity.MerchantInfo;
import com.hzq.user.entity.Product;
import com.hzq.user.service.MerchantInfoService;
import com.hzq.user.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/**
 * Created by hzq on 16/12/11.
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    ProductService productService;
    @Autowired
    MerchantInfoService merchantInfoService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderRecordMapper orderRecordMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String initOrderPay(OrderParam orderParam) {
        Integer productId = orderParam.getProductId();
        String orderNo = orderParam.getOrderNo();
        BigDecimal orderAmount = orderParam.getOrderAmount();

        Product product = Optional.ofNullable(productService.getProductById(productId)).orElseThrow(() -> new OrderBizException("商品不存在"));

        Integer merchantId = product.getMerchantId();
        MerchantInfo merchantInfo = Optional.ofNullable(merchantInfoService.getMerchantInfoById(merchantId)).orElseThrow(() -> new OrderBizException("商户不存在"));

        //检查订单是否存在
        Order order = orderMapper.getOrderByOrderNo(orderNo);
        if (order == null) {
            order = OrderUtil.buildOrder(orderParam, merchantInfo, product);
            orderMapper.insert(order);
        } else {
            // 订单存在
            if (order.getOrderAmount().compareTo(orderAmount) != 0)
                throw new OrderBizException("错误订单");
            if (OrderStatusEnume.PAY_SUCCESS.getVal().equals(order.getStatus()))
                throw new OrderBizException("订单已支付成功,无需重复支付");
        }

        // 更新支付订单（因为支付方式可能会变换）
        order.setPayWay(orderParam.getPayWay());
        orderMapper.update(order);

        OrderRecord orderRecord = OrderUtil.buildOrderRecord(order);
        orderRecordMapper.insert(orderRecord);

        //发送http请求,返回returnMessage
        orderRecord.setBankReturnMsg("银行返回结果");
        orderRecordMapper.update(orderRecord);

        return "支付成功";
    }


}
