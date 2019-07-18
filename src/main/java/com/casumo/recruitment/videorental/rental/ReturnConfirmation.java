package com.casumo.recruitment.videorental.rental;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public class ReturnConfirmation {

    private List<Rental> rentals;
    private BigDecimal surcharge;
}
