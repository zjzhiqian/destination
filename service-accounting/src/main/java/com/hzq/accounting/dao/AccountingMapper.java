package com.hzq.accounting.dao;

import com.hzq.accounting.entity.Accounting;
import com.hzq.base.dao.Dao;

public interface AccountingMapper extends Dao<Accounting,Integer> {

    Accounting getAccountingByVoucherNo(String voucherNo);

    Accounting getAccountingByBankOrderNo(String bankOrderNo);
}