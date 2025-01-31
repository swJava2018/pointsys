package com.pointsys.exception;

public class PointExpLimitExceededException extends BaseCodeException {
    public PointExpLimitExceededException(String msg) {
        super(ErrorCode.POINT_EXPIRATION_LIMIT_EXCEEDED, msg);
    }
}