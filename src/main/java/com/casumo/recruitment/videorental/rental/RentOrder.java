package com.casumo.recruitment.videorental.rental;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentOrder {
    private List<RentFilmEntry> films = new ArrayList<>();
}
