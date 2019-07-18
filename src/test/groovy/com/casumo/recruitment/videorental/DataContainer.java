package com.casumo.recruitment.videorental;

import com.casumo.recruitment.videorental.customer.Customer;
import com.casumo.recruitment.videorental.customer.CustomerRepository;
import com.casumo.recruitment.videorental.customer.PersonalData;
import com.casumo.recruitment.videorental.film.Film;
import com.casumo.recruitment.videorental.film.FilmRepository;
import com.casumo.recruitment.videorental.film.FilmType;
import com.casumo.recruitment.videorental.rental.RentalRepository;
import com.casumo.recruitment.videorental.sharedkernel.time.TimeProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataContainer {

    private final FilmRepository filmRepository;
    private final CustomerRepository customerRepository;
    private final RentalRepository rentalRepository;
    private final TimeProvider timeProvider;

    public void initialize() {

    }

    public void initializeFilms() {
        filmRepository.save(matrix());
        filmRepository.save(spiderMan());
    }

    public void initializeCustomers() {
        customerRepository.save(customer());
    }

    public Customer customer() {
        return Customer.builder()
                .id(1L)
                .bonusPoints(10)
                .personalData(PersonalData.builder()
                        .email("hello@casumo.com")
                        .firstName("Andrew")
                        .lastName("Nowak")
                        .build())
                .build();
    }

    public Film spiderMan() {
        return Film.builder()
                .id(1L)
                .barCode("F1002")
                .title("Spider Man")
                .type(FilmType.REGULAR)
                .build();
    }

    public Film matrix() {
        return Film.builder()
                .id(2L)
                .barCode("F1001")
                .title("Matrix 11")
                .type(FilmType.NEW_RELEASE)
                .build();
    }
}
