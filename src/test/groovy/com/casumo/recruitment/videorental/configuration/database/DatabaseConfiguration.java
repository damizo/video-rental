package com.casumo.recruitment.videorental.configuration.database;

import com.casumo.recruitment.videorental.configuration.customer.InMemoryCustomerRepository;
import com.casumo.recruitment.videorental.configuration.film.InMemoryFilmRepository;
import com.casumo.recruitment.videorental.configuration.rental.InMemoryRentalOrderRepository;
import com.casumo.recruitment.videorental.configuration.rental.InMemoryRentalRepository;
import com.casumo.recruitment.videorental.customer.CustomerRepository;
import com.casumo.recruitment.videorental.film.FilmRepository;
import com.casumo.recruitment.videorental.infrastructure.DataContainer;
import com.casumo.recruitment.videorental.rental.RentalOrderRepository;
import com.casumo.recruitment.videorental.rental.RentalRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {
    @Bean
    public DataContainer dataContainer(FilmRepository filmRepository, CustomerRepository customerRepository,
                                       RentalRepository rentalRepository, RentalOrderRepository rentalOrderRepository) {
        return new DataContainer(filmRepository, customerRepository, rentalRepository, rentalOrderRepository);
    }

    @Bean
    public RentalRepository rentalRepository() {
        return new InMemoryRentalRepository();
    }

    @Bean
    public RentalOrderRepository rentalOrderRepository() {
        return new InMemoryRentalOrderRepository();
    }

    @Bean
    public FilmRepository filmRepository() {
        return new InMemoryFilmRepository();
    }

    @Bean
    public CustomerRepository customerRepository() {
        return new InMemoryCustomerRepository();
    }
}
