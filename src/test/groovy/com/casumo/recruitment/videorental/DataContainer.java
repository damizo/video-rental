package com.casumo.recruitment.videorental;

import com.casumo.recruitment.videorental.customer.Customer;
import com.casumo.recruitment.videorental.customer.CustomerRepository;
import com.casumo.recruitment.videorental.customer.PersonalData;
import com.casumo.recruitment.videorental.film.Film;
import com.casumo.recruitment.videorental.film.FilmRepository;
import com.casumo.recruitment.videorental.film.FilmType;
import com.casumo.recruitment.videorental.rental.RentFilmEntry;
import com.casumo.recruitment.videorental.rental.RentalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataContainer {

    private final FilmRepository filmRepository;
    private final CustomerRepository customerRepository;
    private final RentalRepository rentalRepository;


    public void initializeFilms() {
        filmRepository.save(matrix());
        filmRepository.save(spiderMan());
        filmRepository.save(spiderManBetterOne());
        filmRepository.save(outOfAfrica());
    }

    public void cleanUp() {
        filmRepository.deleteAll();
        rentalRepository.deleteAll();
        customerRepository.deleteAll();
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

    public Film spiderManBetterOne() {
        return Film.builder()
                .id(3L)
                .barCode("F1003")
                .title("Spider Man 2")
                .type(FilmType.REGULAR)
                .build();
    }

    public Film outOfAfrica() {
        return Film.builder()
                .id(4L)
                .barCode("F1004")
                .title("Out of Africa")
                .type(FilmType.OLD)
                .build();
    }

    public RentFilmEntry matrixEntry(Integer days) {
        return new RentFilmEntry(matrix().getId(), days);
    }

    public RentFilmEntry spiderManEntry(Integer days) {
        return new RentFilmEntry(spiderMan().getId(), days);
    }

    public RentFilmEntry spiderManBetterOneEntry(Integer days) {
        return new RentFilmEntry(spiderManBetterOne().getId(), days);
    }

    public RentFilmEntry outOfAfricaEntry(Integer days) {
        return new RentFilmEntry(outOfAfrica().getId(), days);
    }
}
