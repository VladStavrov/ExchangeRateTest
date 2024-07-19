package com.example.exchange_rate_service.exeptions;

import lombok.Getter;

@Getter
public class ApiException extends Exception {
    private final int statusCode;
    private final ApiErrorResponse errorResponse;

    public ApiException(int statusCode, ApiErrorResponse errorResponse) {
        super(errorResponse.getTitle());
        this.statusCode = statusCode;
        this.errorResponse = errorResponse;
    }
}
