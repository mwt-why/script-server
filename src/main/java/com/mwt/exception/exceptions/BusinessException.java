package com.mwt.exception.exceptions;

public class BusinessException extends RuntimeException {
    private String msg;

    private Throwable cause;

    public BusinessException() {

    }

    public BusinessException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BusinessException(Throwable cause) {
        super(cause);
        this.cause = cause;
    }

    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
        this.msg = msg;
        this.cause = cause;
    }
}
