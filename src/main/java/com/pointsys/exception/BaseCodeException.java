package com.pointsys.exception;

public class BaseCodeException extends RuntimeException {
    private final ErrorCode errorCode;

    protected BaseCodeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    protected BaseCodeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
