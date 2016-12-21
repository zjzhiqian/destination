package com.hzq.order.enums;

/**
 * 订单状态枚举类
 * Created by hzq on 16/10/9.
 */
public enum OrderStatusEnume {

    WAIT_PAY("待支付", 0),
    PAYING("正在支付", 1),
    PAY_SUCCESS("支付成功", 2),
    PAY_FAIL("支付失败", 3),
    CANCEL("取消", 4);

    private String context;
    private Integer val;

    OrderStatusEnume(String context, Integer val) {
        this.context = context;
        this.val = val;
    }

    public static String getContext(Integer status) {
        for (OrderStatusEnume types : OrderStatusEnume.values()) {
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
