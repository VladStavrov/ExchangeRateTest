package com.example.exchange_rate_service.repositories;

import com.example.exchange_rate_service.models.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate,Long> {
    Optional<ExchangeRate> findByDateAndCurAbbreviation(LocalDate date, String curAbbreviation);
}
