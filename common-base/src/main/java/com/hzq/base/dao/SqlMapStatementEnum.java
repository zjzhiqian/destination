package com.hzq.base.dao;

/**
 * Created by hzq on 16/10/17.
 */
public enum SqlMapStatementEnum {
    GET_BY_ID(".getByPk", "根据ID去查询的通用statement"),
    INSERT_OBJECT(".insert", "插入对象的通用statement");

    private String statementId;
    private String desc;

    private SqlMapStatementEnum(String statementId, String desc) {
        this.statementId = statementId;
        this.desc = desc;
    }

    public static String getExtNameByCode(String statementId) {
        SqlMapStatementEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            SqlMapStatementEnum e = var1[var3];
            if (e.getStatementId().equals(statementId)) {
                return e.desc;
            }
        }

        return null;
    }

    public String getStatementId() {
        return this.statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
