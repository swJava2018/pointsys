package com.pointsys.controller;

import com.pointsys.exception.BaseCodeException;
import com.pointsys.exception.ErrorCode;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestControllerAdvice {
    public static final String CODE = "code";

    @Nonnull
    @ExceptionHandler(BaseCodeException.class)
    public ProblemDetail handleBaseCodeException(BaseCodeException cause, HttpServletRequest request) {
        ProblemDetail result = ProblemDetail.forStatusAndDetail(
                cause.getErrorCode().asStatusCode(),
                cause.getMessage());
        ErrorCode errorCode = cause.getErrorCode();
        result.setProperty(CODE, errorCode.getCode());
        return result;
    }

    @Nonnull
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception cause, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ProblemDetail result = ProblemDetail.forStatusAndDetail(
                errorCode.asStatusCode(),
                cause.getMessage());
        result.setProperty(CODE, errorCode.getCode());
        return result;
    }
}