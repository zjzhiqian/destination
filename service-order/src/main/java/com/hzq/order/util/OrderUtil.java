package com.hzq.order.util;

import com.alibaba.fastjson.JSON;
import com.hzq.accounting.entity.AccountingMessage;
import com.hzq.base.util.Creator;
import com.hzq.base.util.UUIDUtils;
import com.hzq.message.entity.Message;
import com.hzq.message.enums.MessageQueueName;
import com.hzq.order.entity.Order;
import com.hzq.order.entity.OrderParam;
import com.hzq.order.entity.OrderRecord;
import com.hzq.order.enums.OrderStatusEnume;
import com.hzq.user.entity.MerchantInfo;
import com.hzq.user.entity.Product;

import java.math.BigDecimal;

/**
 * Created by hzq on 16/12/11.
 */
public class OrderUtil {

    /**
     * 生成order实体
     */
    public static Order buildOrder(OrderParam orderParam, MerchantInfo merchantInfo, Product product) {
        Order order = Creator.newInstance(orderParam, Order.class);
        order.setMerchantName(merchantInfo.getUserName());
        order.setMerchantId(merchantInfo.getId());
        order.setProductName(product.getName());
        order.setStatus(OrderStatusEnume.WAIT_PAY.getVal());
        order.setCreater("自动生成");
        order.setOrderFrom("自动生成");
        order.setRefererUrl("");
        return order;
    }


    public static OrderRecord buildOrderRecord(Order order) {
        OrderRecord record = Creator.newInstance(order, OrderRecord.class);
        record.setId(null);
        record.setCreatedAt(null);
        record.setUpdatedAt(null);
        record.setStatus(OrderStatusEnume.WAIT_PAY.getVal());
        BigDecimal orderAmount = order.getOrderAmount();
        BigDecimal feeRate = BigDecimal.ONE;
        //平台收入 = 订单金额 * 支付费率(设置的费率除以100为真实费率)
        BigDecimal platIncome = orderAmount.multiply(feeRate.divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
        //平台成本 = 订单金额 * 微信费率(设置的费率除以100为真实费率)
        BigDecimal platCost = orderAmount.multiply(new BigDecimal("0.6")).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal platProfit = platIncome.subtract(platCost);//平台利润 = 平台收入 - 平台成本
        record.setFeeRate(feeRate);//费率
        record.setPlatCost(platCost);//平台成本
        record.setPlatIncome(platIncome);//平台收入
        record.setPlatProfit(platProfit);//平台利润
        return record;
    }

    public static Message buildAccountingMessage(OrderRecord orderRecord) {
        //封装会计需要的实体
        AccountingMessage accounting = new AccountingMessage();
        accounting.setVoucherNo(orderRecord.getTrxNo()); //银行流水号
        accounting.setProfit(orderRecord.getPlatProfit());
        accounting.setIncome(orderRecord.getPlatIncome());
        accounting.setCost(orderRecord.getPlatCost());
        accounting.setRemark("测试分布式");
        accounting.setBankChangeAmount(orderRecord.getOrderAmount());
        accounting.setReceiverAccountNo(orderRecord.getMerchantId() + "");
        accounting.setBankOrderNo(orderRecord.getBankOrderNo());
        String uuid = UUIDUtils.get32UUID();
        accounting.setMessageId(uuid);
        JSON.toJSONString(accounting);
        Message message = new Message(uuid, MessageQueueName.ACCOUNT_NOTIFY.name(), JSON.toJSONString(accounting));
        message.setField1(orderRecord.getBankOrderNo()); //**消息业务关联,查询消息是否被消费 定时器查询条件
        return message;
    }
}
