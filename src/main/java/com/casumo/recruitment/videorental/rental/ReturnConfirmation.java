package com.casumo.recruitment.videorental.rental;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ReturnConfirmation {

    private List<Rental> rentals;
    private BigDecimal surcharge;
}
