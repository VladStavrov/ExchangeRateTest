package com.example.exchange_rate_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateResponse {
    private String message;
    private List<ExchangeRateDTO> result;
}