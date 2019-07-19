package com.casumo.recruitment.videorental.rental;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class RentConfirmation {
    private List<Long> rentalIds;
    private BigDecimal totalPrice;
}
