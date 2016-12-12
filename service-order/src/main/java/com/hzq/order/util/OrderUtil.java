package com.hzq.order.util;

import com.hzq.base.util.Creator;
import com.hzq.order.entity.Order;
import com.hzq.order.entity.OrderParam;
import com.hzq.order.entity.OrderRecord;
import com.hzq.order.enums.OrderStatusEnume;
import com.hzq.user.entity.MerchantInfo;
import com.hzq.user.entity.Product;
import org.slf4j.Logger;
import org.springframework.util.IdGenerator;
import org.springframework.util.SimpleIdGenerator;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hzq on 16/12/11.
 */
public class OrderUtil {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(OrderUtil.class);


    private static final IdGenerator idGenerator = new SimpleIdGenerator();

    /**
     * 生成order实体
     */
    public static Order buildOrder(OrderParam orderParam, MerchantInfo merchantInfo, Product product) {
        logger.info("封装order实体...");

        Order order = Creator.newInstance(orderParam, Order.class);
        order.setMerchantName(merchantInfo.getUserName());
        order.setMerchantId(merchantInfo.getId());
        order.setProductName(product.getName());
        order.setStatus(OrderStatusEnume.WAIT_PAY.getVal());
        order.setCreater("自动生成");
        order.setOrderFrom("自动生成");
        order.setRefererUrl("");
//        private String cancelReason;
//        private String trxType;
//        private String fundIntoType;
//        private Integer isRefund;
//        private Integer refundTimes;
//        private BigDecimal successRefundAmount;
//        private String trxNo;
        return order;
    }


    public static OrderRecord buildOrderRecord(Order order) {
        //TODO  测试:默认事务隔离级别(read write)下 对一个数据查询出来 进行+操作 然后update 有没有问题!
        logger.info("封装orderRecord实体...");
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

        record.setBankOrderNo(order.getOrderNo()); //让银行订单号与商户订单号相同，方便做处理
        //银行流水号
        String uuid = idGenerator.generateId().toString();
        uuid = uuid.substring(uuid.lastIndexOf("-") + 1); //12位
        String txNo = new SimpleDateFormat("MMddHHmmssSSS").format(new Date()) + uuid;
        record.setTrxNo(txNo);
//        private Date completeTime;
//        private String bankTrxNo;
//        private String payerUserNo;
//        private String payerName;
//        private BigDecimal payerPayAmount;
//        private BigDecimal payerFee;
//        private String payerAccountType;
//        private String receiverUserNo;
//        private String receiverName;
//        private BigDecimal receiverPayAmount;
//        private BigDecimal receiverFee;
//        private String bankReturnMsg;
        return record;
    }

}
