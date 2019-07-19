package com.casumo.recruitment.videorental.configuration.rental;

import com.casumo.recruitment.videorental.configuration.TimeConfiguration;
import com.casumo.recruitment.videorental.configuration.customer.CustomerConfiguration;
import com.casumo.recruitment.videorental.configuration.database.DatabaseConfiguration;
import com.casumo.recruitment.videorental.configuration.film.FilmConfiguration;
import com.casumo.recruitment.videorental.customer.CustomerRepository;
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
                                     CustomerRepository customerRepository, TimeProvider timeProvider) {
        return new RentalFacade(customerRepository, filmRepository, rentalRepository, timeProvider);
    }

    @Bean
    public RentalController rentalController(RentalFacade rentalFacade) {
        return new RentalController(rentalFacade);
    }

}
