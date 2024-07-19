package com.example.exchange_rate_service.models;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "exchange_rates", uniqueConstraints = {@UniqueConstraint(columnNames = {"curAbbreviation","date"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    @Id
    @GeneratedValue
    private Long id;
    private int curId;
    private LocalDate date;
    private String curAbbreviation;
    private int curScale;
    private String curName;
    private double curOfficialRate;
}