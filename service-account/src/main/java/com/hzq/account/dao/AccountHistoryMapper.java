package com.hzq.account.dao;

import com.hzq.account.entity.AccountHistory;
import com.hzq.base.dao.Dao;
import org.apache.ibatis.annotations.Param;

public interface AccountHistoryMapper extends Dao<AccountHistory,Integer> {

    AccountHistory getAccountHistoryByRequestNo(String requestNo);

    int changeStatusById(@Param("id") Integer id, @Param("preVal") Integer preVal,@Param("endVal") Integer endVal);
}