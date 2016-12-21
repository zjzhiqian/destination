package com.hzq.account.dao;

import com.hzq.account.entity.AccountHistory;
import com.hzq.base.dao.Dao;

public interface AccountHistoryMapper extends Dao<AccountHistory,Integer> {

    AccountHistory getAccountHistoryByRequestNo(String requestNo);
}