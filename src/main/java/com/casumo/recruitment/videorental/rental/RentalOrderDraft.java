package com.casumo.recruitment.videorental.rental;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalOrderDraft {
    private List<RentFilmEntry> films = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public void addEntry(RentFilmEntry entry) {
        films.add(entry);
    }

    public void totalPrice(BigDecimal price) {
        this.totalPrice = this.totalPrice.add(price);
    }

}
