package com.casumo.recruitment.videorental.rental;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class RentConfirmation {
    private List<Long> rentalId;
    private BigDecimal price;
}
