package com.casumo.recruitment.videorental.rental;

import com.casumo.recruitment.videorental.customer.Customer;
import com.casumo.recruitment.videorental.film.FilmType;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Rental {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition = "CUSTOMER_ID")
    private Customer customer;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate rentDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expectedReturnDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate actualReturnDate;

    @Enumerated(EnumType.STRING)
    private FilmType filmType;


    public void returnFilm(LocalDate returnDate) {
        this.actualReturnDate = returnDate;
    }

    public Integer getExpectedDaysOfRental() {
        return BigDecimal.valueOf(DAYS.between(rentDate, expectedReturnDate)).intValue();
    }

    public BigDecimal getPrice() {
        Integer expectedDaysOfRental = getExpectedDaysOfRental();
        return filmType.calculatePrice(expectedDaysOfRental);
    }

    public BigDecimal getSurcharge() {
        Integer expectedDaysOfRental = getExpectedDaysOfRental();
        Integer lateDaysOfRental = getLateDaysOfRental();
        return filmType.calculateSurcharge(lateDaysOfRental, expectedDaysOfRental);
    }

    private Integer getLateDaysOfRental() {
        long lateDays = DAYS.between(expectedReturnDate, actualReturnDate);
        long noLateDays = 0;
        return BigDecimal.valueOf(lateDays <= noLateDays ? noLateDays : lateDays).intValue();
    }
}
