package com.casumo.recruitment.videorental.rental;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class RentOrderDTO {

    private Long id;
    private List<RentalDTO> rentals;
    private BigDecimal totalPrice;
    private BigDecimal totalSurcharge;

}
