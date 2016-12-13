package com.hzq.order.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *  支付回调接口
 * Created by hzq on 16/12/12.
 */
public class OrderNotify implements Serializable{

    private String resultCode;
    private String transactionId;
    private Date timeEnd;
    private String outTradeNo;
    private String messageId;//回调时发送到mq的messageId

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
}
