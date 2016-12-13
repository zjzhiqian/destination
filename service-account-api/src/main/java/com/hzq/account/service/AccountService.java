package com.hzq.account.service;

import com.hzq.account.entity.Account;

import java.math.BigDecimal;

/**
 * Created by hzq on 16/12/13.
 */
public interface AccountService {
    /**
     * 给账户加款并生成流水
     * @param merchantId  商户id
     * @param amount 商户金额
     * @param bankOrderNo 银行订单号
     * @param bankTrxNo 银行流水号
     * @return 账户
     */
    Account addAmountToMerchant(Integer merchantId, BigDecimal amount, String bankOrderNo, String bankTrxNo);
}
