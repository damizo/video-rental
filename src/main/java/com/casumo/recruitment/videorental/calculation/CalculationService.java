package com.casumo.recruitment.videorental.calculation;

import com.casumo.recruitment.videorental.film.FilmType;
import com.casumo.recruitment.videorental.sharedkernel.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
@RequiredArgsConstructor
public class CalculationService {
    private final InitialCalculationStrategy newReleaseFilmCalculationStrategy;
    private final InitialCalculationStrategy oldFilmCalculationStrategy;
    private final InitialCalculationStrategy regularFilmCalculationStrategy;

    private final SurchargeCalculationStrategy extraDaysSurchargeCalculationStrategy;
    private final SurchargeCalculationStrategy standardSurchargeCalculationStrategy;

    private final TimeProvider timeProvider;

    public BigDecimal calculateSurchargePrice(FilmType filmType, LocalDate expectedReturnDate, LocalDate actualReturnDate) {
        long daysAboveStandardOfferRentalDays = DAYS.between(expectedReturnDate, timeProvider.today()) - filmType.getNumberOfReliefDays();
        switch (filmType) {
            case OLD:
            case REGULAR:
                return extraDaysSurchargeCalculationStrategy.calculate(daysAboveStandardOfferRentalDays, filmType);
            case NEW_RELEASE:
                return standardSurchargeCalculationStrategy.calculate(daysAboveStandardOfferRentalDays, filmType);
            default:
                throw new IllegalArgumentException(String.format("No calculation option find for film type: %s", filmType));
        }
    }

    public BigDecimal calculateRentPrice(LocalDate expectedReturnDate, FilmType filmType) {
        long declaredDaysOfRental = DAYS.between(expectedReturnDate, timeProvider.today());
        switch (filmType) {
            case NEW_RELEASE:
                return newReleaseFilmCalculationStrategy.calculate(declaredDaysOfRental, filmType);
            case REGULAR:
                return regularFilmCalculationStrategy.calculate(declaredDaysOfRental, filmType);
            case OLD:
                return oldFilmCalculationStrategy.calculate(declaredDaysOfRental, filmType);
            default:
                throw new IllegalArgumentException(String.format("No calculation option find for film type: %s", filmType));
        }
    }
}
