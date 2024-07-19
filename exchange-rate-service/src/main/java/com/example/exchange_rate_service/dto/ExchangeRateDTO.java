package com.example.exchange_rate_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateDTO {
    private Long id;
    private int curId;
    private LocalDate date;
    private String curAbbreviation;
    private int curScale;
    private String curName;
    private double curOfficialRate;
}
