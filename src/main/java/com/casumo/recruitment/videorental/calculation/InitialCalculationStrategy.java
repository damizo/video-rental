package com.casumo.recruitment.videorental.calculation;

import com.casumo.recruitment.videorental.film.FilmType;

import java.math.BigDecimal;

public interface InitialCalculationStrategy {
    BigDecimal calculate(long daysOfRental, FilmType filmType);
}
