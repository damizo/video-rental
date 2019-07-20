package com.casumo.recruitment.videorental.rental;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RentFilmEntry {
    private Long filmId;
    private Integer numberOfDays;
    private BigDecimal price;
}
