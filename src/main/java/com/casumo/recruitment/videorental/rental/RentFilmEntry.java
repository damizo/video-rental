package com.casumo.recruitment.videorental.rental;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RentFilmEntry {
    private Long filmId;
    private LocalDate expectedReturnDate;
}
