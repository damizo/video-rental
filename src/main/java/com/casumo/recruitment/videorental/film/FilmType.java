package com.casumo.recruitment.videorental.film;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public enum FilmType {


    NEW_RELEASE(new StandardCalculationStrategy(Prices.PREMIUM_PRICE), BonusPoints.TWO),
    REGULAR(new FirstDaysCalculationStrategy(3, Prices.BASIC_PRICE, Prices.BASIC_PRICE), BonusPoints.ONE),
    OLD(new FirstDaysCalculationStrategy(5, Prices.BASIC_PRICE, Prices.BASIC_PRICE), BonusPoints.ONE);

    private final CalculationStrategy calculationStrategy;

    @Getter
    private final Integer bonusPoints;

    public BigDecimal calculatePrice(Integer numberOfActualRentalDays) {
        return calculationStrategy.calculatePrice(numberOfActualRentalDays);
    }

    public BigDecimal calculateSurcharge(Integer numberOfActualRentalDays, Integer numberOfExpectedRentalDays) {
        return calculationStrategy.calculateSurcharge(numberOfActualRentalDays, numberOfExpectedRentalDays);
    }

    private interface CalculationStrategy {
        BigDecimal calculatePrice(Integer numberOfActualRentalDays);

        BigDecimal calculateSurcharge(Integer numberOfActualRentalDays, Integer numberOfExpectedRentalDays);
    }

    @RequiredArgsConstructor
    private static class StandardCalculationStrategy implements CalculationStrategy {
        private final BigDecimal price;

        public BigDecimal calculatePrice(Integer numberOfActualRentalDays) {
            return price.multiply(BigDecimal.valueOf(numberOfActualRentalDays));
        }

        @Override
        public BigDecimal calculateSurcharge(Integer numberOfLateDays, Integer numberOfExpectedRentalDays) {
            return calculatePrice(numberOfLateDays);
        }
    }

    @RequiredArgsConstructor
    private static class FirstDaysCalculationStrategy implements CalculationStrategy {

        private final Integer numberOfDaysWithRelief;
        private final BigDecimal initialPrice;
        private final BigDecimal surchargePrice;

        @Override
        public BigDecimal calculatePrice(Integer numberOfActualRentalDays) {
            BigDecimal priceForDaysWithoutRelief = BigDecimal.ZERO;
            if (numberOfActualRentalDays > numberOfDaysWithRelief) {
                priceForDaysWithoutRelief = initialPrice.multiply(BigDecimal.valueOf((numberOfActualRentalDays - numberOfDaysWithRelief)));
            }
            return initialPrice.add(priceForDaysWithoutRelief);
        }

        @Override
        public BigDecimal calculateSurcharge(Integer numberOfLateDays, Integer numberOfExpectedRentalDays) {
            if (numberOfExpectedRentalDays + numberOfLateDays <= numberOfDaysWithRelief) {
                return BigDecimal.ZERO;
            }
            return surchargePrice.multiply(BigDecimal.valueOf(numberOfLateDays));
        }

    }


    private static final class Prices {
        private static final BigDecimal BASIC_PRICE = new BigDecimal(30);
        private static final BigDecimal PREMIUM_PRICE = new BigDecimal(40);
    }

    private static final class BonusPoints {
        private static final Integer ONE = 1;
        private static final Integer TWO = 2;
    }
}
