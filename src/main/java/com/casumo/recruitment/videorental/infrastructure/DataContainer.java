package com.casumo.recruitment.videorental.infrastructure;

import com.casumo.recruitment.videorental.customer.Customer;
import com.casumo.recruitment.videorental.customer.CustomerRepository;
import com.casumo.recruitment.videorental.film.Film;
import com.casumo.recruitment.videorental.film.FilmRepository;
import com.casumo.recruitment.videorental.film.FilmType;
import com.casumo.recruitment.videorental.rental.RentalOrderRepository;
import com.casumo.recruitment.videorental.rental.RentalRepository;
import com.casumo.recruitment.videorental.shared.CurrencyType;
import com.casumo.recruitment.videorental.shared.domain.PersonalData;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(Profiles.TEST)
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
                .currency(CurrencyType.SEK)
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

}
