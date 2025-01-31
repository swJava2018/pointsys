package com.pointsys.exception;

public class PointAlreadyUsedException extends BaseCodeException {
    public PointAlreadyUsedException(String msg) {
        super(ErrorCode.POINT_ALREADY_USED, msg);

    }
}