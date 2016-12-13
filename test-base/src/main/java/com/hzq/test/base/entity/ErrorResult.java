package com.hzq.test.base.entity;

/**
 * Created by hzq on 16/12/11.
 */
public class ErrorResult {
    private int code;
    private String error;

    /**
     * Constructs an error by status code and error message.
     *
     * @param code  The HTTP status code.
     * @param error The error message
     */
    public ErrorResult(int code, String error) {
        this.code = code;
        this.error = error;
    }

    /**
     * Get the HTTP status code
     *
     * @return The HTTP status code
     */
    public int getCode() {
        return code;
    }

    /**
     * Set the HTTP status code.
     *
     * @param code The HTTP status code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Get the error message.
     *
     * @return The error message
     */
    public String getError() {
        return error;
    }

    /**
     * Set the error message.
     *
     * @param error The error message
     */
    public void setError(String error) {
        this.error = error;
    }
}

