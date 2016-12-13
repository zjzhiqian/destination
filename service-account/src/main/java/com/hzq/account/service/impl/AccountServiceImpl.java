package com.hzq.account.service.impl;

import com.hzq.account.dao.AccountHistoryMapper;
import com.hzq.account.dao.AccountMapper;
import com.hzq.account.entity.Account;
import com.hzq.account.entity.AccountHistory;
import com.hzq.account.service.AccountService;
import com.hzq.base.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 账户服务
 * Created by hzq on 16/12/13.
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountMapper accountMapper;
    @Autowired
    AccountHistoryMapper accountHistoryMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Account addAmountToMerchant(Integer merchantId, BigDecimal amount, String bankOrderNo, String bankTrxNo) {

        Account account = accountMapper.getAccountByMerchantId(merchantId);

        accountMapper.addAmountByMerchantId(merchantId, amount);
        // 记录账户历史
        AccountHistory accountHistory = new AccountHistory();
        accountHistory.setMerchantId(merchantId);
        accountHistory.setAmount(amount);
        accountHistory.setBalance(account.getBalance());//TODO 这样会有问题
        accountHistory.setRequestNo(bankOrderNo);
        accountHistory.setBankTrxNo(bankTrxNo);
        accountHistory.setAccountNo(account.getAccountNo());
        accountHistory.setStatus(1);
        accountHistoryMapper.insert(accountHistory);
        return account;
    }

}
