package com.pointsys.exception;

public class PointSaveLimitExceededException extends BaseCodeException {
    public PointSaveLimitExceededException(String msg) {
        super(ErrorCode.POINT_SAVE_LIMIT_EXCEEDED, msg);
    }
}
