package com.casumo.recruitment.videorental.configuration.rental;

import com.casumo.recruitment.videorental.configuration.TimeConfiguration;
import com.casumo.recruitment.videorental.configuration.customer.CustomerConfiguration;
import com.casumo.recruitment.videorental.configuration.database.DatabaseConfiguration;
import com.casumo.recruitment.videorental.configuration.film.FilmConfiguration;
import com.casumo.recruitment.videorental.customer.CustomerRepository;
import com.casumo.recruitment.videorental.film.FilmFacade;
import com.casumo.recruitment.videorental.film.FilmRepository;
import com.casumo.recruitment.videorental.rental.*;
import com.casumo.recruitment.videorental.shared.time.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({FilmConfiguration.class, CustomerConfiguration.class, TimeConfiguration.class, DatabaseConfiguration.class})
public class RentalConfiguration {

    @Bean
    public RentalFacade rentalFacade(RentalRepository rentalRepository, FilmRepository filmRepository,
                                     CustomerRepository customerRepository, RentalOrderRepository rentalOrderRepository,
                                     RentalFactory rentalFactory, TimeProvider timeProvider, RentalMapper rentalOrderMapper) {
        return new RentalFacade(customerRepository, filmRepository, rentalRepository,
                rentalOrderRepository, rentalFactory, rentalOrderMapper, timeProvider);
    }

    @Bean
    public RentalBoxStorage rentalBoxStorage() {
        return new RentalBoxStorage();
    }

    @Bean
    public RentalFactory rentalFactory() {
        return new RentalFactory();
    }

    @Bean
    public RentalMapper rentalOrderMapper() {
        return new RentalMapper();
    }

    @Bean
    public RentalController rentalController(RentalFacade rentalFacade, RentalBoxStorage rentDraftStorage, FilmFacade filmFacade) {
        return new RentalController(rentalFacade, filmFacade, rentDraftStorage);
    }

}
