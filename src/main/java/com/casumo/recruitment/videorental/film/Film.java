package com.casumo.recruitment.videorental.film;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Film {

    @Id
    @GeneratedValue
    private Long id;
    private String barCode;
    private String title;
    private FilmType type;

    public BigDecimal checkPrice(Integer numberOfDays) {
        return type.calculatePrice(numberOfDays);
    }
}
