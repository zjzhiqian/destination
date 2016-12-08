package com.hzq.base.exception;

public class DataAccessException extends org.springframework.dao.DataAccessException {
    private static final long serialVersionUID = 3352628982583502006L;

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
