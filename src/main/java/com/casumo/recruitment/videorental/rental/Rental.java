package com.casumo.recruitment.videorental.rental;

import com.casumo.recruitment.videorental.customer.Customer;
import com.casumo.recruitment.videorental.film.FilmType;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@Data
@Entity
public class Rental {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(columnDefinition = "CUSTOMER_ID")
    private Customer customer;

    @DateTimeFormat
    private LocalDate rentDate;

    @DateTimeFormat
    private LocalDate expectedReturnDate;

    @DateTimeFormat
    private LocalDate actualReturnDate;

    @Enumerated(EnumType.STRING)
    private FilmType filmType;


    public void returnFilm() {
        actualReturnDate = LocalDate.now();
    }

}
