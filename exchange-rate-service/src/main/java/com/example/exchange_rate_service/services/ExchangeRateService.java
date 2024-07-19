package com.example.exchange_rate_service.services;

import com.example.exchange_rate_service.client.ExchangeRateApiClient;
import com.example.exchange_rate_service.dto.ExchangeRateAPIDTO;
import com.example.exchange_rate_service.dto.ExchangeRateDTO;
import com.example.exchange_rate_service.dto.ExchangeRateResponse;
import com.example.exchange_rate_service.exeptions.ApiException;
import com.example.exchange_rate_service.models.ExchangeRate;
import com.example.exchange_rate_service.repositories.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository repository;
    private final ModelMapper modelMapper;
    private final ExchangeRateApiClient exchangeRateApiClient;

    /**
     * Метод для получения и сохранения курсов валют на указанную дату
     */
    public ExchangeRateResponse fetchAndStoreExchangeRates(LocalDate date) {
        List<ExchangeRateAPIDTO> rates = fetchRatesFromApi(date);
        if (rates == null || rates.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Данные не были найдены");
        }

        List<ExchangeRate> exchangeRates = rates.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

        List<ExchangeRate> updatedExchangeRates = saveOrUpdateExchangeRates(exchangeRates);

        List<ExchangeRateDTO> responseExchangeRates = updatedExchangeRates.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return new ExchangeRateResponse(
                "Данные " + date + " были успешно сохранены.",
                responseExchangeRates
        );
    }

    /**
     * Метод для получения курсов валют с внешнего API
     */
    private List<ExchangeRateAPIDTO> fetchRatesFromApi(LocalDate date) {
        try {
            return exchangeRateApiClient.fetchExchangeRates(date);
        } catch (ApiException e) {
            HttpStatus status = HttpStatus.valueOf(e.getStatusCode());
            throw new ResponseStatusException(status, e.getErrorResponse().getTitle());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error fetching exchange rates", e);
        }
    }

    /**
     * Метод для сохранения или обновления курсов валют в базе данных
     */
    private List<ExchangeRate> saveOrUpdateExchangeRates(List<ExchangeRate> exchangeRates) {
        List<ExchangeRate> updatedExchangeRates = new ArrayList<>();
        for (ExchangeRate exchangeRate : exchangeRates) {
            Optional<ExchangeRate> existingRate = repository.findByDateAndCurAbbreviation(
                    exchangeRate.getDate(), exchangeRate.getCurAbbreviation());
            ExchangeRate updatedExchangeRate;
            updatedExchangeRate = existingRate.map(rate -> updateExistingRate(rate, exchangeRate)).orElseGet(() -> repository.save(exchangeRate));
            updatedExchangeRates.add(updatedExchangeRate);
        }
        return  updatedExchangeRates;
    }

    /**
     * Метод для обновления существующего курса валют
     */
    private ExchangeRate updateExistingRate(ExchangeRate existing, ExchangeRate updated) {
        existing.setCurScale(updated.getCurScale());
        existing.setCurName(updated.getCurName());
        existing.setCurOfficialRate(updated.getCurOfficialRate());
        return repository.save(existing);
    }

    /**
     * Метод для получения курса валют по дате и коду валюты
     */
    public ExchangeRateDTO getExchangeRateByDateAndCode(LocalDate date, String currencyCode) {
        ExchangeRate exchangeRate = repository.findByDateAndCurAbbreviation(date, currencyCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Курс валюты не найден для даты: " + date + " и кода валюты: " + currencyCode));
        return toDTO(exchangeRate);
    }

    /**
     * Метод для преобразования объекта ExchangeRate в DTO
     */
    public ExchangeRateDTO toDTO(ExchangeRate exchangeRate) {
        return modelMapper.map(exchangeRate, ExchangeRateDTO.class);
    }

    /**
     * Метод для преобразования DTO в объект ExchangeRate
     */
    public ExchangeRate toEntity(ExchangeRateAPIDTO exchangeRateDTO) {
        return modelMapper.map(exchangeRateDTO, ExchangeRate.class);
    }
}