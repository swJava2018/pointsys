package com.pointsys.exception;

public class PointBalanceLimitExceededException extends BaseCodeException {
    public PointBalanceLimitExceededException(String msg) {
        super(ErrorCode.POINT_BALANCE_LIMIT_EXCEEDED, msg);
    }
}