package com.hzq.accounting.exception;

import com.hzq.base.exception.BizException;

/**
 * Created by hzq on 16/12/11.
 */
public class AccountingException extends BizException {

    public AccountingException() {
        super();
    }

    public AccountingException(String message) {
        super(message);
    }

}
