package com.example.exchange_rate_service.controllers;

import com.example.exchange_rate_service.dto.ExchangeRateDTO;
import com.example.exchange_rate_service.dto.ExchangeRateResponse;
import com.example.exchange_rate_service.services.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/exchange-rates")
@RequiredArgsConstructor
@Tag(name = "ExchangeRate", description = "Operations pertaining to exchange rates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping("/fetch-and-store")
    @Operation(summary = "Fetch and store exchange rates", description = "Fetch exchange rates from external API and store them in the database",
            parameters = {
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "date",
                            required = true,
                            description = "Date for which to fetch exchange rates",
                            schema = @Schema(type = "string", format = "date")
                    )
            })
    @ApiResponse(responseCode = "200", description = "Exchange rates fetched and stored successfully", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ExchangeRateResponse.class))
    })
    @ApiResponse(responseCode = "400", description = "Some errors")
    @ApiResponse(responseCode = "404", description = "Data not found")
    public ResponseEntity<ExchangeRateResponse> fetchAndStoreExchangeRates(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        ExchangeRateResponse response = exchangeRateService.fetchAndStoreExchangeRates(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get exchange rate by date and currency code", description = "Get the exchange rate for a specific date and currency code",
            parameters = {
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "date",
                            required = true,
                            description = "Date for which to get the exchange rate",
                            schema = @Schema(type = "string", format = "date")
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "currencyCode",
                            required = true,
                            description = "Currency code for which to get the exchange rate",
                            schema = @Schema(type = "string")
                    )
            })
    @ApiResponse(responseCode = "200", description = "Exchange rate found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ExchangeRateDTO.class))
    })
    @ApiResponse(responseCode = "404", description = "Exchange rate not found")
    public ResponseEntity<ExchangeRateDTO> getExchangeRateByDateAndCode(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("currencyCode") String currencyCode) {
        ExchangeRateDTO exchangeRate = exchangeRateService.getExchangeRateByDateAndCode(date, currencyCode);
        return ResponseEntity.ok(exchangeRate);
    }
}