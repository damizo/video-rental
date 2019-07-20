package com.casumo.recruitment.videorental.rental;

import com.casumo.recruitment.videorental.customer.Customer;
import com.casumo.recruitment.videorental.film.FilmType;
import com.casumo.recruitment.videorental.shared.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Builder
@AllArgsConstructor
@Data
@Entity
public class Rental {

    @Id
    @GeneratedValue
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
    private CurrencyType currency = CurrencyType.SEK;

    @Enumerated(EnumType.STRING)
    private FilmType filmType;

    @Enumerated(EnumType.STRING)
    private RentalStatus rentalStatus = RentalStatus.STARTED;

    public Rental returnFilm(LocalDate returnDate) {
        this.actualReturnDate = returnDate;
        this.rentalStatus = RentalStatus.END;
        return this;
    }

    public Integer getExpectedDaysOfRental() {
        return BigDecimal.valueOf(DAYS.between(rentDate, expectedReturnDate)).intValue();
    }

    public BigDecimal getPrice() {
        Integer expectedDaysOfRental = getExpectedDaysOfRental();
        return filmType.calculatePrice(expectedDaysOfRental);
    }

    public BigDecimal getSurcharge() {
        if (!isReturned()) {
            return BigDecimal.ZERO;
        }

        Integer expectedDaysOfRental = getExpectedDaysOfRental();
        Integer lateDaysOfRental = getLateDaysOfRental();
        return filmType.calculateSurcharge(lateDaysOfRental, expectedDaysOfRental);
    }

    private Integer getLateDaysOfRental() {
        long lateDays = DAYS.between(expectedReturnDate, actualReturnDate);
        long noLateDays = 0;
        return BigDecimal.valueOf(lateDays <= noLateDays ? noLateDays : lateDays).intValue();
    }

    private boolean isReturned() {
        return Optional.ofNullable(actualReturnDate).isPresent();
    }
}
