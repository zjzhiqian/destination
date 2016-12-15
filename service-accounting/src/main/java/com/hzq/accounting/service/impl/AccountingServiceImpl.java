package com.hzq.accounting.service.impl;

import com.hzq.accounting.dao.AccountingMapper;
import com.hzq.accounting.entity.Accounting;
import com.hzq.accounting.service.AccountingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * Created by hzq on 16/12/13.
 */
@Service("accountingService")
public class AccountingServiceImpl implements AccountingService {

    @Autowired
    AccountingMapper accountingMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int generateAccounting(Accounting accounting) {
        //TODO  这里insert会插入多条 数据库作唯一索引
        int result = 0;
        if (accountingMapper.getAccountingByVoucherNo(accounting.getVoucherNo()) == null) {
            result = accountingMapper.insert(accounting);
        }
        return result;
    }

    @Override
    public Accounting getAccountingByBankOrderNo(String bankOrderNo) {
        return accountingMapper.getAccountingByBankOrderNo(bankOrderNo);
    }
}
