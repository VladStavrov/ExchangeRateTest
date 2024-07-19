package com.example.exchange_rate_service.client;

import com.example.exchange_rate_service.dto.ExchangeRateAPIDTO;
import com.example.exchange_rate_service.exeptions.ApiErrorResponse;
import com.example.exchange_rate_service.exeptions.ApiException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

@Component
public class ExchangeRateApiClient {

    private static final String BASE_URL = "https://www.nbrb.by/api/exrates";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ExchangeRateApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }


    /*
    Метод для получения курсов валют на заданную дату
    */
    public List<ExchangeRateAPIDTO> fetchExchangeRates(LocalDate date) throws IOException, InterruptedException, ApiException {
        URI uri = buildUri(date);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        String responseBody = response.body();
        if (statusCode == 200) {
            return objectMapper.readValue(responseBody, new TypeReference<List<ExchangeRateAPIDTO>>() {});

        } else {
            ApiErrorResponse errorResponse = objectMapper.readValue(responseBody, ApiErrorResponse.class);
            throw new ApiException(statusCode, errorResponse);
        }
    }

    /*
    Метод для построения URI для запроса курсов валют
    */
    private URI buildUri(LocalDate date) {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/rates")
                .queryParam("ondate", date.toString())
                .queryParam("periodicity", "0")
                .build()
                .toUri();
    }
}