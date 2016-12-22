package com.hzq.accounting.service.impl;

import com.alibaba.fastjson.JSON;
import com.hzq.accounting.dao.AccountingMapper;
import com.hzq.accounting.entity.Accounting;
import com.hzq.accounting.service.AccountingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by hzq on 16/12/13.
 */
@Service("accountingService")
public class AccountingServiceImpl implements AccountingService {

    @Autowired
    AccountingMapper accountingMapper;
    private Logger logger = LoggerFactory.getLogger(AccountingServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int generateAccounting(Accounting accounting) {

        int result = 0;
        try {
            if (accountingMapper.getAccountingByVoucherNo(accounting.getVoucherNo()) == null)
                result = accountingMapper.insert(accounting);
        } catch (Exception e) {
            logger.error("生成accounting记录异常{}", JSON.toJSONString(accounting), e);
        }
        return result;
    }

    @Override
    public Accounting getAccountingByBankOrderNo(String bankOrderNo) {
        return accountingMapper.getAccountingByBankOrderNo(bankOrderNo);
    }
}
