package com.casumo.recruitment.videorental.film;


import java.math.BigDecimal;

public enum FilmType {

    NEW_RELEASE {
        @Override
        public BigDecimal getBasicPrice() {
            throw new UnsupportedOperationException();
        }

        @Override
        public BigDecimal getPremiumPrice() {
            return Prices.PREMIUM_PRICE;
        }

        @Override
        public Integer getNumberOfReliefDays() {
            throw new ReliefNotAvailableException();
        }

        @Override
        public boolean hasFirstDaysRelief() {
            return false;
        }

        @Override
        public Integer getBonusPoints() {
            return 3;
        }
    },
    REGULAR {
        @Override
        public BigDecimal getBasicPrice() {
            return Prices.BASIC_PRICE;
        }

        @Override
        public BigDecimal getPremiumPrice() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Integer getNumberOfReliefDays() {
            return 3;
        }

        @Override
        public boolean hasFirstDaysRelief() {
            return true;
        }

        @Override
        public Integer getBonusPoints() {
            return 1;
        }
    },
    OLD {
        @Override
        public BigDecimal getBasicPrice() {
            return Prices.BASIC_PRICE;
        }

        @Override
        public BigDecimal getPremiumPrice() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Integer getNumberOfReliefDays() {
            return 5;
        }

        @Override
        public boolean hasFirstDaysRelief() {
            return true;
        }

        @Override
        public Integer getBonusPoints() {
            return 1;
        }

    };

    public abstract BigDecimal getBasicPrice();

    public abstract BigDecimal getPremiumPrice();

    public abstract Integer getNumberOfReliefDays();

    public abstract boolean hasFirstDaysRelief();

    public abstract Integer getBonusPoints();

    private static final class Prices {
        private static final BigDecimal BASIC_PRICE = new BigDecimal(30);
        private static final BigDecimal PREMIUM_PRICE = new BigDecimal(40);
    }
}
