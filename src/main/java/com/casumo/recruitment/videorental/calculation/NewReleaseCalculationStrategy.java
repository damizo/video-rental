package com.casumo.recruitment.videorental.calculation;

import com.casumo.recruitment.videorental.film.FilmType;

import java.math.BigDecimal;

public class NewReleaseCalculationStrategy implements InitialCalculationStrategy {

    @Override
    public BigDecimal calculate(long daysOfRental, FilmType filmType) {
        return filmType.getPremiumPrice().multiply(BigDecimal.valueOf(daysOfRental));
    }
}
