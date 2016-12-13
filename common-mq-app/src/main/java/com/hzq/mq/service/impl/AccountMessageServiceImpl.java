package com.hzq.mq.service.impl;

import com.hzq.accounting.entity.Accounting;
import com.hzq.accounting.entity.AccountingMessage;
import com.hzq.accounting.service.AccountingService;
import com.hzq.base.util.Creator;
import com.hzq.message.service.MessageService;
import com.hzq.mq.service.AccountMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hzq on 16/12/13.
 */
@Service
public class AccountMessageServiceImpl implements AccountMessageService {

    @Autowired
    AccountingService accountingService;

    @Autowired
    MessageService messageService;

    @Override
    public void completeAccounting(AccountingMessage accountingMessage) {
        Accounting accounting = Creator.newInstance(accountingMessage, Accounting.class);
        accountingService.generateAccounting(accounting);
        messageService.deleteMessageByMessageId(accountingMessage.getMessageId());
    }
}
