package com.example.exchange_rate_service.exeptions;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class ApiErrorResponse {
    private int status;
    private String title;
    private String type;
    private String traceId;
    private Map<String, String[]> errors;
}