package com.casumo.recruitment.videorental.rental;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class RentalOrder {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Rental> rentals = new ArrayList<>();

    public RentalOrder(List<Rental> rentals) {
        this.rentals = rentals;
    }

    public BigDecimal getTotalPrice() {
        return rentals.stream()
                .map(Rental::getPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getTotalSurcharge() {
        return rentals.stream()
                .map(Rental::getSurcharge)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
