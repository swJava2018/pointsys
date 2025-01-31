package com.pointsys.exception;

import org.springframework.http.HttpStatus;

import java.util.Optional;

public enum ErrorCode {
    INVALID_REQUEST(400_000),
    NOT_FOUND(404_000),
    INTERNAL_SERVER_ERROR(500_000),

    // POINT_*
    POINT_SAVE_LIMIT_EXCEEDED(400_001),
    POINT_EXPIRATION_LIMIT_EXCEEDED(400_002),
    POINT_BALANCE_LIMIT_EXCEEDED(400_003),
    POINT_INSUFFICIENT_BALANCE(400_004),
    POINT_ALREADY_USED(400_005),
    POINT_NOT_FOUND(404_001),
    POINT_SPEC_NOT_FOUND(404_002);

    final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public HttpStatus asStatusCode() {
        return Optional.ofNullable(HttpStatus.resolve(this.code / 1000))
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
