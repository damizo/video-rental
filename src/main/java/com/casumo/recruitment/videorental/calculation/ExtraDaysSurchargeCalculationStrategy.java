package com.casumo.recruitment.videorental.calculation;

import com.casumo.recruitment.videorental.film.FilmType;

import java.math.BigDecimal;

public class ExtraDaysSurchargeCalculationStrategy implements SurchargeCalculationStrategy {
    @Override
    public BigDecimal calculate(long numberOfLateDays, FilmType filmType) {
        return BigDecimal.ZERO;
    }
}
