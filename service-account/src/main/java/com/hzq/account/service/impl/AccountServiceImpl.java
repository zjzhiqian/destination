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
    @Transactional
    public Account addAmountToMerchant(Integer merchantId, BigDecimal amount, String bankOrderNo, String bankTrxNo) {
        Account account = accountMapper.getAccountByMerchantId(merchantId);
        Date lastModifyDate = account.getUpdatedAt();
        if (!DateUtils.isSameDayWithToday(lastModifyDate)) {
            account.setTodayExpend(BigDecimal.ZERO);
            account.setTodayIncome(BigDecimal.ZERO);
        }
        // 总收益累加和今日收益
        account.setTotalIncome(account.getTotalIncome().add(amount));
        if (DateUtils.isSameDayWithToday(lastModifyDate)) {
            account.setTodayIncome(account.getTodayIncome().add(amount));
        } else {
            account.setTodayIncome(amount);
        }
        account.setBalance(account.getBalance().add(amount));
        // 记录账户历史
        AccountHistory accountHistory = new AccountHistory();
        accountHistory.setMerchantId(merchantId);
        accountHistory.setAmount(amount);
        accountHistory.setBalance(account.getBalance());
        accountHistory.setRequestNo(bankOrderNo);
        accountHistory.setBankTrxNo(bankTrxNo);
        accountHistory.setAccountNo(account.getAccountNo());
        accountHistory.setStatus(1);
        accountHistoryMapper.insert(accountHistory);
        accountMapper.update(account);
        return account;
    }

}
