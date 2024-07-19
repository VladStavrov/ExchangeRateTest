package com.example.exchange_rate_service.services;

import com.example.exchange_rate_service.client.ExchangeRateApiClient;
import com.example.exchange_rate_service.dto.ExchangeRateAPIDTO;
import com.example.exchange_rate_service.dto.ExchangeRateDTO;
import com.example.exchange_rate_service.dto.ExchangeRateResponse;
import com.example.exchange_rate_service.exeptions.ApiErrorResponse;
import com.example.exchange_rate_service.exeptions.ApiException;
import com.example.exchange_rate_service.models.ExchangeRate;
import com.example.exchange_rate_service.repositories.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ExchangeRateApiClient exchangeRateApiClient;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    private ExchangeRateAPIDTO exchangeRateAPIDTO;
    private ExchangeRate exchangeRate;
    private ExchangeRateDTO exchangeRateDTO;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        date = LocalDate.now();
        exchangeRateAPIDTO = new ExchangeRateAPIDTO();
        exchangeRate = new ExchangeRate();
        exchangeRateDTO = new ExchangeRateDTO();

        when(modelMapper.map(exchangeRateAPIDTO, ExchangeRate.class)).thenReturn(exchangeRate);
        when(modelMapper.map(exchangeRate, ExchangeRateDTO.class)).thenReturn(exchangeRateDTO);
    }

    @Test
    void fetchAndStoreExchangeRates_Success() throws IOException, InterruptedException, ApiException {
        when(exchangeRateApiClient.fetchExchangeRates(any(LocalDate.class))).thenReturn(Arrays.asList(exchangeRateAPIDTO));
        when(repository.findByDateAndCurAbbreviation(any(LocalDate.class), anyString())).thenReturn(Optional.empty());
        when(repository.save(any(ExchangeRate.class))).thenReturn(exchangeRate);
        ExchangeRateResponse response = exchangeRateService.fetchAndStoreExchangeRates(date);
        assertNotNull(response);
        assertEquals("Данные " + date + " были успешно сохранены.", response.getMessage());
        verify(repository, times(1)).save(any(ExchangeRate.class));
    }
    @Test
    void fetchAndStoreExchangeRates_NotFound() throws IOException, InterruptedException {
        try {
            when(exchangeRateApiClient.fetchExchangeRates(any(LocalDate.class))).thenReturn(null);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            exchangeRateService.fetchAndStoreExchangeRates(date);
        });

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode().value());
    }

    @Test
    void fetchAndStoreExchangeRates_ApiException() throws IOException, InterruptedException, ApiException {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setTitle("Error fetching exchange rates");
        when(exchangeRateApiClient.fetchExchangeRates(any(LocalDate.class))).thenThrow(new ApiException(404, errorResponse));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            exchangeRateService.fetchAndStoreExchangeRates(date);
        });

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode().value());
    }
    @Test
    void getExchangeRateByDateAndCode_Success() {
        when(repository.findByDateAndCurAbbreviation(any(LocalDate.class), anyString())).thenReturn(Optional.of(exchangeRate));

        ExchangeRateDTO result = exchangeRateService.getExchangeRateByDateAndCode(date, "USD");

        assertNotNull(result);
        assertEquals(exchangeRateDTO, result);
    }
    @Test
    void getExchangeRateByDateAndCode_NotFound() {
        when(repository.findByDateAndCurAbbreviation(any(LocalDate.class), anyString())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            exchangeRateService.getExchangeRateByDateAndCode(date, "USD");
        });

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode().value());
    }
}