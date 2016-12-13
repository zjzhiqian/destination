package com.hzq.accounting.service;

import com.hzq.accounting.entity.Accounting;

/**
 * Created by hzq on 16/12/13.
 */
public interface AccountingService {

    int generateAccounting(Accounting accounting);

    Accounting getAccountingByBankOrderNo(String bankOrderNo);
}
