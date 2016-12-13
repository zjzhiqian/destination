package com.hzq.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.hzq.account.service.AccountService;
import com.hzq.message.entity.Message;
import com.hzq.message.service.MessageService;
import com.hzq.order.dao.OrderMapper;
import com.hzq.order.dao.OrderRecordMapper;
import com.hzq.order.entity.Order;
import com.hzq.order.entity.OrderNotify;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.IdGenerator;
import org.springframework.util.SimpleIdGenerator;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @Autowired
    MessageService messageService;
    @Autowired
    AccountService accountService;

    private static final IdGenerator idGenerator = new SimpleIdGenerator();

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


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

        OrderRecord record = OrderUtil.buildOrderRecord(order);
        orderRecordMapper.insert(record);

        //发送http请求,返回returnMessage bankOrderNo与TrxNo(银行流水号)
        record.setBankOrderNo(order.getOrderNo()); //让银行订单号与商户订单号相同，方便做处理
        String uuid = idGenerator.generateId().toString();
        uuid = uuid.substring(uuid.lastIndexOf("-") + 1); //12位
        String txNo = new SimpleDateFormat("MMddHHmmssSSS").format(new Date()) + uuid;
        record.setTrxNo(txNo);
        orderRecordMapper.update(record);
        return "生成订单 发起支付成功";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completePay(OrderNotify orderNotify) {
        String returnMessage = JSON.toJSONString(orderNotify);
        logger.info("接收到支付结果{}", returnMessage);

        String bankOrderNo = orderNotify.getOutTradeNo();
        OrderRecord orderRecord = orderRecordMapper.getOrderRecordByBankOrderNo(bankOrderNo);

        if (orderRecord == null)
            throw new OrderBizException("订单支付记录不存在");
        if (!OrderStatusEnume.WAIT_PAY.getVal().equals(orderRecord.getStatus())) {
            logger.info("订单为非待支付状态,不作处理");
            return;
        }
        boolean isSuccess = false;
        if ("success".equalsIgnoreCase(orderNotify.getResultCode()))
            isSuccess = true;

        orderRecord.setBankReturnMsg(returnMessage);
        if (isSuccess) {
            //返回结果成功
            Message message = OrderUtil.buildAccountingMessage(orderRecord);
            messageService.preSaveMessage(message);
            try {
                completeSuccessOrder(orderRecord, orderNotify);
            } catch (Exception e) {
                e.printStackTrace();
                throw new OrderBizException();
            }
            messageService.confirmAndSendMessage(message.getMessageId());
        } else {
            //返回结果失败
            completeFailOrder(orderRecord);
        }
    }

    @Override
    public OrderRecord getOrderRecordByBankNo(String bankOrderNo) {
        return orderRecordMapper.getOrderRecordByBankOrderNo(bankOrderNo);
    }

    /**
     * 收到银行支付成功消息
     */
    private void completeSuccessOrder(OrderRecord orderRecord, OrderNotify orderNotify) {
        //TODO 这里应该做TCC事务
        orderRecord.setBankReturnMsg(JSON.toJSONString(orderNotify)); //银行返回消息
        orderRecord.setCompleteTime(orderNotify.getTimeEnd()); //支付时间
        orderRecord.setBankTrxNo(orderNotify.getTransactionId()); //银行流水号
        orderRecord.setStatus(OrderStatusEnume.PAY_SUCCESS.getVal()); //支付成功
        orderRecordMapper.update(orderRecord);
        Order order = orderMapper.getOrderByOrderNo(orderRecord.getOrderNo());
        order.setStatus(OrderStatusEnume.PAY_SUCCESS.getVal()); //支付成功
        orderMapper.update(order);

        BigDecimal amount = orderRecord.getOrderAmount().subtract(orderRecord.getPlatIncome());
        //给商户加款
        accountService.addAmountToMerchant(orderRecord.getMerchantId(), amount, orderRecord.getBankOrderNo(), orderRecord.getBankTrxNo());




    }

    /**
     * 收到银行支付失败消息
     *
     * @param orderRecord
     */
    private void completeFailOrder(OrderRecord orderRecord) {
        orderRecord.setStatus(OrderStatusEnume.PAY_FAIL.getVal());
        orderRecordMapper.update(orderRecord);

        Order order = orderMapper.getOrderByOrderNo(orderRecord.getOrderNo());
        order.setStatus(OrderStatusEnume.PAY_FAIL.getVal());
        orderMapper.update(order);
    }


}
