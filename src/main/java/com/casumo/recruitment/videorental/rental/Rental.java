package com.casumo.recruitment.videorental.rental;

import com.casumo.recruitment.videorental.customer.Customer;
import com.casumo.recruitment.videorental.film.FilmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private FilmType filmType;

    @Enumerated(EnumType.STRING)
    private RentalStatus rentalStatus;

    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal surcharge = BigDecimal.ZERO;

    public void rent(LocalDate rentDate) {
        this.rentalStatus = RentalStatus.STARTED;
        this.rentDate = rentDate;
        this.price = calculatePrice();
    }

    public Rental returnFilm(LocalDate returnDate) {
        this.actualReturnDate = returnDate;
        this.rentalStatus = RentalStatus.END;
        this.surcharge = calculateSurcharge();
        return this;
    }

    private BigDecimal calculateSurcharge() {
        if (!isReturned()) {
            return BigDecimal.ZERO;
        }

        Integer expectedDaysOfRental = getExpectedDaysOfRental();
        Integer lateDaysOfRental = getLateDaysOfRental();
        return filmType.calculateSurcharge(lateDaysOfRental, expectedDaysOfRental);
    }

    private BigDecimal calculatePrice() {
        Integer expectedDaysOfRental = getExpectedDaysOfRental();
        return filmType.calculatePrice(expectedDaysOfRental);
    }

    private Integer getExpectedDaysOfRental() {
        return BigDecimal.valueOf(DAYS.between(rentDate, expectedReturnDate)).intValue();
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
