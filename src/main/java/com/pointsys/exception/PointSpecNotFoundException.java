package com.pointsys.exception;

public class PointSpecNotFoundException extends NotFoundException {
    public PointSpecNotFoundException() {
        super(ErrorCode.POINT_SPEC_NOT_FOUND);
    }
}
