package com.hzq.account.enume;

/**
 * Created by hzq on 16/12/21.
 */
public enum AccountHistoryStatusEnum {

    TRYING("处理中", 0),

    CONFORM("已确认", 1),

    CANCEL("取消", 2);

    private String context;
    private Integer val;

    AccountHistoryStatusEnum(String context, Integer val) {
        this.context = context;
        this.val = val;
    }

    public static String getContext(Integer status) {
        for (AccountHistoryStatusEnum types : AccountHistoryStatusEnum.values()) {
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
