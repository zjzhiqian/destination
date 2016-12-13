package com.hzq.mq.service;

import com.hzq.accounting.entity.AccountingMessage;

/**
 * Created by hzq on 16/12/13.
 */
public interface AccountMessageService {
    void completeAccounting(AccountingMessage accountingMessage);

}
