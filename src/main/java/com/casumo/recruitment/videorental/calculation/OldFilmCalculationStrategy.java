package com.casumo.recruitment.videorental.calculation;

import com.casumo.recruitment.videorental.film.FilmType;

import java.math.BigDecimal;

public class OldFilmCalculationStrategy implements InitialCalculationStrategy {
    @Override
    public BigDecimal calculate(long daysOfRental, FilmType filmType) {
        BigDecimal price = filmType.getBasicPrice();
        if (daysOfRental > filmType.getNumberOfReliefDays()) {
            price = calculatePriceForDaysWithoutRelief(daysOfRental - filmType.getNumberOfReliefDays(), filmType.getBasicPrice());
        }
        return price;
    }

    private BigDecimal calculatePriceForDaysWithoutRelief(long extraDays, BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(extraDays));
    }
}
