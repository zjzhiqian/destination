package com.hzq.account.service.impl;

import com.hzq.account.dao.AccountHistoryMapper;
import com.hzq.account.dao.AccountMapper;
import com.hzq.account.entity.Account;
import com.hzq.account.entity.AccountHistory;
import com.hzq.account.enume.AccountHistoryStatusEnum;
import com.hzq.account.service.AccountService;
import com.hzq.base.redis.RedisHelper;
import org.mengyun.tcctransaction.Compensable;
import org.mengyun.tcctransaction.api.TransactionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

    private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);


    /**
     * try阶段,只生成了账户历史
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Compensable(confirmMethod = "confirmAddAmountToMerchant", cancelMethod = "cancelAddAmountToMerchant")
    public void addAmountToMerchant(TransactionContext transactionContext, Integer merchantId, BigDecimal amount, String bankOrderNo, String bankTrxNo) {
        if (RedisHelper.isKeyExist("AccountServiceImpl:addAmountToMerchant:" + bankOrderNo, 10))
            throw new RuntimeException("try生成账户历史过于频繁..");
        logger.warn("addAmountToMerchant 方法开始调用 ,bankOrderNo:{},时间{}", bankOrderNo, new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        AccountHistory accountHistoryEntity = accountHistoryMapper.getAccountHistoryByRequestNo(bankOrderNo);
        if (accountHistoryEntity == null) {
            Account account = accountMapper.getAccountByMerchantId(merchantId);
            AccountHistory accountHistory = new AccountHistory();
            accountHistory.setMerchantId(merchantId);
            accountHistory.setAmount(amount);
            accountHistory.setBalance(account.getBalance());
            accountHistory.setRequestNo(bankOrderNo);
            accountHistory.setBankTrxNo(bankTrxNo);
            accountHistory.setAccountNo(account.getAccountNo());
            accountHistory.setStatus(AccountHistoryStatusEnum.TRYING.getVal());
            logger.warn("addAmountToMerchant 方法插入数据 ,bankOrderNo:{},时间{}", bankOrderNo, new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
            accountHistoryMapper.insert(accountHistory);
        } else if (AccountHistoryStatusEnum.CANCEL.getVal().equals(accountHistoryEntity.getStatus())) {
            accountHistoryEntity.setStatus(AccountHistoryStatusEnum.TRYING.getVal());
            accountHistoryMapper.update(accountHistoryEntity);
        }
        logger.warn("addAmountToMerchant 方法调用结束 ,bankOrderNo:{},时间{}", bankOrderNo, new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
    }


    /**
     * confirm阶段,+款并且修改账户历史的状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmAddAmountToMerchant(TransactionContext transactionContext, Integer merchantId, BigDecimal amount, String bankOrderNo, String bankTrxNo) {
        if (RedisHelper.isKeyExist("AccountServiceImpl:ConFirmOrCancelAddAmountToMerchant:" + bankOrderNo, 10))
            throw new RuntimeException("confirming账户过于频繁..");
        logger.warn("confirmAddAmountToMerchant 方法开始调用 ,bankOrderNo:{},时间{}", bankOrderNo, new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        AccountHistory accountHistory = accountHistoryMapper.getAccountHistoryByRequestNo(bankOrderNo);
        if (accountHistory == null) throw new RuntimeException("confirm出错!账户修改历史不存在");
        if (!AccountHistoryStatusEnum.TRYING.getVal().equals(accountHistory.getStatus()))
            return;
        accountHistory.setStatus(AccountHistoryStatusEnum.CONFORM.getVal());
        int rs = accountHistoryMapper.changeStatusById(accountHistory.getId(), AccountHistoryStatusEnum.TRYING.getVal(), AccountHistoryStatusEnum.CONFORM.getVal());
        if (rs <= 0) {
            logger.warn("confirmAddAmountToMerchant 方法开始调用中,更新账户状态失败,accountHistory状态:{},bankOrderNo:{},时间{}", accountHistory.getStatus(), bankOrderNo, new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
            throw new RuntimeException("更新账户状态失败!!!");
        }
        accountMapper.addAmountByMerchantId(merchantId, amount);
        logger.warn("confirmAddAmountToMerchant 方法结束调用 ,bankOrderNo:{},时间{}", bankOrderNo, new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
    }

    /**
     * cancel阶段,将账户历史状态修改为cancel
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelAddAmountToMerchant(TransactionContext transactionContext, Integer merchantId, BigDecimal amount, String bankOrderNo, String bankTrxNo) {
        if (RedisHelper.isKeyExist("AccountServiceImpl:ConFirmOrCancelAddAmountToMerchant:" + bankOrderNo, 10))
            throw new RuntimeException("canceling账户过于频繁..");
        logger.warn("cancelAddAmountToMerchant 方法开始调用 ,bankOrderNo:{},时间{}", bankOrderNo, new Date());
        AccountHistory accountHistory = accountHistoryMapper.getAccountHistoryByRequestNo(bankOrderNo);
        if (accountHistory == null || !AccountHistoryStatusEnum.TRYING.getVal().equals(accountHistory.getStatus()))
            return;
        int rs = accountHistoryMapper.changeStatusById(accountHistory.getId(), AccountHistoryStatusEnum.TRYING.getVal(), AccountHistoryStatusEnum.CANCEL.getVal());
        if (rs <= 0) {
            logger.warn("cancelAddAmountToMerchant 方法调用中,更新账户状态失败,accountHistory状态:{} ,bankOrderNo:{},时间{}", accountHistory.getStatus(), bankOrderNo, new Date());
            throw new RuntimeException("更新账户状态失败!!!");
        }
        accountHistoryMapper.update(accountHistory);
        logger.warn("cancelAddAmountToMerchant 方法结束调用,bankOrderNo:{},时间{}", bankOrderNo, new Date());
    }


}
