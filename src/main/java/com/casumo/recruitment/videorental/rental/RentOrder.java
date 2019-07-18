package com.casumo.recruitment.videorental.rental;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class RentOrder {
    private List<RentFilmEntry> films = new ArrayList<>();
}
