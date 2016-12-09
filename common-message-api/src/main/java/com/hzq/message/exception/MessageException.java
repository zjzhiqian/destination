package com.hzq.message.exception;

/**
 * Created by hzq on 16/12/8.
 */
public class MessageException extends RuntimeException {
    public MessageException(){}

    public MessageException(String message) {
        super(message);
    }
}
