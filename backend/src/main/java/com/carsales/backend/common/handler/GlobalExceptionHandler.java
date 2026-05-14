package com.carsales.backend.common.handler;

import com.carsales.backend.common.api.ApiResponse;
import com.carsales.backend.common.exception.BizException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BizException.class)
    public ApiResponse<Void> handleBiz(BizException e) {
        return ApiResponse.fail(e.getCode(), e.getMessage());
    }

    @SuppressWarnings("null")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleMethodArgNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError() == null
                ? "invalid request"
                : e.getBindingResult().getFieldError().getDefaultMessage();
        return ApiResponse.fail(400, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<Void> handleConstraintViolation(ConstraintViolationException e) {
        return ApiResponse.fail(400, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleEx(Exception e) {
        return ApiResponse.fail(500, e.getMessage());
    }
}
