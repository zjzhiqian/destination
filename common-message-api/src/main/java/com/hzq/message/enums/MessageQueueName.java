package com.hzq.message.enums;

/**
 * Created by hzq on 16/12/12.
 */
public enum MessageQueueName {

    ORDER_NOTIFY("订单队列", 0);


    private String context;
    private Integer val;

    MessageQueueName(String context, Integer val) {
        this.context = context;
        this.val = val;
    }

    public static String getContext(Integer status) {
        for (MessageQueueName types : MessageQueueName.values()) {
            if (types.getVal().intValue() == status) {
                return types.getContext();
            }
        }
        return "该类型不存在";
    }


    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

}
