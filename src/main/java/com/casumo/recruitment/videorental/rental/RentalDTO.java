package com.casumo.recruitment.videorental.rental;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class RentalDTO {

    private Long customerId;
    private LocalDate rentDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private String filmType;
    private String status;
    private BigDecimal price;
    private BigDecimal surcharge;
}
