package com.hzq.account.service.impl;

import com.hzq.account.dao.AccountHistoryMapper;
import com.hzq.account.dao.AccountMapper;
import com.hzq.account.entity.Account;
import com.hzq.account.entity.AccountHistory;
import com.hzq.account.enume.AccountHistoryStatusEnum;
import com.hzq.account.service.AccountService;
import org.mengyun.tcctransaction.Compensable;
import org.mengyun.tcctransaction.api.TransactionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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

    private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);


    @Override
    @Transactional(rollbackFor = Exception.class)
    @Compensable(confirmMethod = "confirmAddAmountToMerchant", cancelMethod = "cancelAddAmountToMerchant")
    public void addAmountToMerchant(TransactionContext transactionContext, Integer merchantId, BigDecimal amount, String bankOrderNo, String bankTrxNo) {

        logger.info("addAmountToMerchant............");
        AccountHistory accountHistoryEntity = accountHistoryMapper.getAccountHistoryByRequestNo(bankOrderNo);
        if (accountHistoryEntity == null) {
            Account account = accountMapper.getAccountByMerchantId(merchantId);
            accountMapper.addAmountByMerchantId(merchantId, amount);
            AccountHistory accountHistory = new AccountHistory();
            accountHistory.setMerchantId(merchantId);
            accountHistory.setAmount(amount);
            accountHistory.setBalance(account.getBalance());
            accountHistory.setRequestNo(bankOrderNo);
            accountHistory.setBankTrxNo(bankTrxNo);
            accountHistory.setAccountNo(account.getAccountNo());
            accountHistory.setStatus(AccountHistoryStatusEnum.TRYING.getVal());
            accountHistoryMapper.insert(accountHistory);
        } else if (AccountHistoryStatusEnum.CANCEL.getVal().equals(accountHistoryEntity.getStatus())) {
            accountHistoryEntity.setStatus(AccountHistoryStatusEnum.TRYING.getVal());
            accountHistoryMapper.update(accountHistoryEntity);
        }
        throw new RuntimeException(""); //TODO 测试用,到时候删除
    }

    @Transactional(rollbackFor = Exception.class)
    public void confirmAddAmountToMerchant(TransactionContext transactionContext, Integer merchantId, BigDecimal amount, String bankOrderNo, String bankTrxNo) {
        logger.info("confirmAddAmountToMerchant............");
        AccountHistory accountHistory = accountHistoryMapper.getAccountHistoryByRequestNo(bankOrderNo);
        //TODO 这里为null的话 需要抛出异常 而不是return
        if (accountHistory == null || !AccountHistoryStatusEnum.TRYING.getVal().equals(accountHistory.getStatus()))
            return;
        accountHistory.setStatus(AccountHistoryStatusEnum.CONFORM.getVal());
        accountHistoryMapper.update(accountHistory);

        Account account = accountMapper.getAccountByMerchantId(merchantId);
        accountMapper.addAmountByMerchantId(merchantId, amount);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelAddAmountToMerchant(TransactionContext transactionContext, Integer merchantId, BigDecimal amount, String bankOrderNo, String bankTrxNo) {
        logger.info("cancelAddAmountToMerchant............");
        AccountHistory accountHistory = accountHistoryMapper.getAccountHistoryByRequestNo(bankOrderNo);
        if (accountHistory == null || !AccountHistoryStatusEnum.TRYING.getVal().equals(accountHistory.getStatus()))
            return;
        accountHistory.setStatus(AccountHistoryStatusEnum.CANCEL.getVal());
        accountHistoryMapper.update(accountHistory);
    }


}
