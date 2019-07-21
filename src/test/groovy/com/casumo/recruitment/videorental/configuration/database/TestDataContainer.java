package com.casumo.recruitment.videorental.configuration.database;

import com.casumo.recruitment.videorental.customer.Customer;
import com.casumo.recruitment.videorental.customer.CustomerDTO;
import com.casumo.recruitment.videorental.customer.CustomerRepository;
import com.casumo.recruitment.videorental.film.Film;
import com.casumo.recruitment.videorental.film.FilmDTO;
import com.casumo.recruitment.videorental.film.FilmRepository;
import com.casumo.recruitment.videorental.infrastructure.DataContainer;
import com.casumo.recruitment.videorental.rental.RentFilmEntryDTO;
import com.casumo.recruitment.videorental.rental.RentalOrderRepository;
import com.casumo.recruitment.videorental.rental.RentalRepository;
import com.casumo.recruitment.videorental.shared.domain.PersonalData;
import com.casumo.recruitment.videorental.shared.dto.PersonalDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TestDataContainer extends DataContainer {

    @Autowired
    public TestDataContainer(FilmRepository filmRepository, CustomerRepository customerRepository, RentalRepository rentalRepository, RentalOrderRepository rentalOrderRepository) {
        super(filmRepository, customerRepository, rentalRepository, rentalOrderRepository);
    }

    public RentFilmEntryDTO matrixEntry(Integer days) {
        return new RentFilmEntryDTO(Identifiers.ONE, days, BigDecimal.ZERO);
    }

    public RentFilmEntryDTO spiderManEntry(Integer days) {
        return new RentFilmEntryDTO(Identifiers.TWO, days, BigDecimal.ZERO);
    }

    public RentFilmEntryDTO outOfAfricaEntry(Integer days) {
        return new RentFilmEntryDTO(Identifiers.FOUR, days, BigDecimal.ZERO);
    }

    public CustomerDTO customerDTO() {
        Customer customer = customer();
        PersonalDataDTO personalData = personalDataDTO(customer.getPersonalData());
        return CustomerDTO.builder()
                .id(Identifiers.ONE)
                .currency(customer.getCurrency().name())
                .bonusPoints(customer.getBonusPoints())
                .personalData(personalData)
                .build();
    }

    private PersonalDataDTO personalDataDTO(PersonalData personalData) {
        return PersonalDataDTO.builder()
                .firstName(personalData.getFirstName())
                .lastName(personalData.getLastName())
                .email(personalData.getEmail())
                .build();
    }

    public FilmDTO matrixDTO() {
        Film matrix = matrix();
        return FilmDTO.builder()
                .id(Identifiers.ONE)
                .title(matrix.getTitle())
                .barCode(matrix.getBarCode())
                .type(matrix.getType().name())
                .build();
    }

    public FilmDTO spiderManDTO() {
        Film spiderMan = spiderMan();
        return FilmDTO.builder()
                .id(Identifiers.TWO)
                .title(spiderMan.getTitle())
                .barCode(spiderMan.getBarCode())
                .type(spiderMan.getType().name())
                .build();
    }


    public FilmDTO spiderManBetterOneDTO() {
        Film spiderManBetterOne = spiderManBetterOne();
        return FilmDTO.builder()
                .id(Identifiers.THREE)
                .title(spiderManBetterOne.getTitle())
                .barCode(spiderManBetterOne.getBarCode())
                .type(spiderManBetterOne.getType().name())
                .build();
    }

    public FilmDTO outOfAfricaDTO() {
        Film outOfAfrica = outOfAfrica();
        return FilmDTO.builder()
                .id(Identifiers.FOUR)
                .title(outOfAfrica.getTitle())
                .barCode(outOfAfrica.getBarCode())
                .type(outOfAfrica.getType().name())
                .build();
    }

    private static final class Identifiers {
        private static final Long ONE = 1L;
        private static final Long TWO = 2L;
        private static final Long THREE = 3L;
        private static final Long FOUR = 4L;
    }

}
