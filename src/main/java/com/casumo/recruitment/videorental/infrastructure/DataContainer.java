package com.casumo.recruitment.videorental.infrastructure;

import com.casumo.recruitment.videorental.customer.Customer;
import com.casumo.recruitment.videorental.customer.CustomerRepository;
import com.casumo.recruitment.videorental.film.Film;
import com.casumo.recruitment.videorental.film.FilmRepository;
import com.casumo.recruitment.videorental.film.FilmType;
import com.casumo.recruitment.videorental.rental.RentFilmEntry;
import com.casumo.recruitment.videorental.rental.RentalOrderRepository;
import com.casumo.recruitment.videorental.rental.RentalRepository;
import com.casumo.recruitment.videorental.shared.domain.PersonalData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class DataContainer {

    private final FilmRepository filmRepository;
    private final CustomerRepository customerRepository;
    private final RentalRepository rentalRepository;
    private final RentalOrderRepository rentalOrderRepository;


    public void initializeCustomers() {
        customerRepository.save(customer());
    }

    public void initializeFilms() {
        filmRepository.save(matrix());
        filmRepository.save(spiderMan());
        filmRepository.save(spiderManBetterOne());
        filmRepository.save(outOfAfrica());
    }

    public void cleanUp() {
        filmRepository.deleteAll();
        rentalRepository.deleteAll();
        rentalOrderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    public Customer customer() {
        return Customer.builder()
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
                .barCode("F1002")
                .title("Spider Man")
                .type(FilmType.REGULAR)
                .build();
    }

    public Film matrix() {
        return Film.builder()
                .barCode("F1001")
                .title("Matrix 11")
                .type(FilmType.NEW_RELEASE)
                .build();
    }

    public Film spiderManBetterOne() {
        return Film.builder()
                .barCode("F1003")
                .title("Spider Man 2")
                .type(FilmType.REGULAR)
                .build();
    }

    public Film outOfAfrica() {
        return Film.builder()
                .barCode("F1004")
                .title("Out of Africa")
                .type(FilmType.OLD)
                .build();
    }

    public RentFilmEntry matrixEntry(Integer days) {
        return new RentFilmEntry(Identifiers.ONE, days, BigDecimal.ZERO);
    }

    public RentFilmEntry spiderManEntry(Integer days) {
        return new RentFilmEntry(Identifiers.TWO, days, BigDecimal.ZERO);
    }

    public RentFilmEntry outOfAfricaEntry(Integer days) {
        return new RentFilmEntry(Identifiers.FOUR, days, BigDecimal.ZERO);
    }

    private static final class Identifiers {
        private static final Long ONE = 1L;
        private static final Long TWO = 2L;
        private static final Long THREE = 3L;
        private static final Long FOUR = 4L;
    }
}
