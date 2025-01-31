package com.pointsys.exception;

public class PointInsufficientBalance extends BaseCodeException {
    public PointInsufficientBalance(String msg) {
        super(ErrorCode.POINT_INSUFFICIENT_BALANCE, msg);
    }
}