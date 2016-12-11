package com.hzq.base.exception;

/**
 * Created by hzq on 16/12/11.
 */
public class BizException extends RuntimeException {

    public BizException(){
        super();
    }
    public BizException(String message){
        super(message);
    }
}
