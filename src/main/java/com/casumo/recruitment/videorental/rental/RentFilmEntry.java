package com.casumo.recruitment.videorental.rental;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentFilmEntry {
    private Long filmId;
    private Integer numberOfDays;
}
