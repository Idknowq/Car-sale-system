package com.carsales.backend.common.handler;

import com.carsales.backend.common.api.ApiResponse;
import com.carsales.backend.common.exception.BizException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BizException.class)
    public ApiResponse<Void> handleBiz(BizException e) {
        return ApiResponse.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleEx(Exception e) {
        return ApiResponse.fail(500, e.getMessage());
    }
}
