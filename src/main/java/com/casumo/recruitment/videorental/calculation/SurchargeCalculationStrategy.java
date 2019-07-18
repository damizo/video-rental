package com.casumo.recruitment.videorental.calculation;

import com.casumo.recruitment.videorental.film.FilmType;

import java.math.BigDecimal;

public interface SurchargeCalculationStrategy {
    BigDecimal calculate(long numberOfLateDays, FilmType filmType);
}
