package com.pointsys.exception;

public class PointNotFoundException extends NotFoundException {
    public PointNotFoundException() {
        super(ErrorCode.POINT_NOT_FOUND);
    }
}
