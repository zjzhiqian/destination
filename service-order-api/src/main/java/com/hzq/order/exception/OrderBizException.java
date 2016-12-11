package com.hzq.order.exception;

import com.hzq.base.exception.BizException;

/**
 * Created by hzq on 16/12/11.
 */
public class OrderBizException extends BizException {

    public OrderBizException() {
        super();
    }

    public OrderBizException(String message) {
        super(message);
    }

}
