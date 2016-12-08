package com.hzq.message.enums;

/**
 * 消息状态枚举类
 * Created by hzq on 16/10/9.
 */
public enum MessageStatus {

    PRE_CONFIRM("待确认", 0),
    CONFIRM("已确认", 1),
    CONSUMED("已消费", 2);


    private String context;
    private Integer val;

    MessageStatus(String context, Integer val) {
        this.context = context;
        this.val = val;
    }

    public static String getContext(Integer status) {
        for (MessageStatus types : MessageStatus.values()) {
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
