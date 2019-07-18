package com.casumo.recruitment.videorental;

import com.casumo.recruitment.videorental.customer.CustomerRepository;
import com.casumo.recruitment.videorental.film.FilmFinder;
import com.casumo.recruitment.videorental.film.FilmRepository;
import com.casumo.recruitment.videorental.film.FilmController;
import com.casumo.recruitment.videorental.rental.RentalFacade;
import com.casumo.recruitment.videorental.rental.RentalRepository;
import com.casumo.recruitment.videorental.rental.RentalResource;
import com.casumo.recruitment.videorental.calculation.CalculationStrategyConfiguration;
import com.casumo.recruitment.videorental.sharedkernel.time.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CalculationStrategyConfiguration.class)
public class FilmRentalConfiguration {

    @Bean
    public FilmFinder filmFacade(FilmRepository filmRepository) {
        return new FilmFinder(filmRepository);
    }

    @Bean
    public FilmController filmResource(FilmFinder filmFacade) {
        return new FilmController(filmFacade);
    }


   /* @Bean
    public RentalFacade rentalFacade(RentalRepository rentalRepository, FilmRepository filmRepository,
                                     CustomerRepository customerRepository) {
        return new RentalFacade(customerRepository, filmRepository, rentalRepository, timeProvider,
                newReleaseFilmCalculationStrategy, oldFilmCalculationStrategy, regularFilmCalculationStrategy);

    }*/

    @Bean
    public TimeProvider timeProvider() {
        return new TimeProvider();
    }

    @Bean
    public RentalResource rentalResource(RentalFacade rentalFacade) {
        return new RentalResource(rentalFacade);
    }

    @Bean
    public DataContainer dataContainer(FilmRepository filmRepository, CustomerRepository customerRepository, RentalRepository rentalRepository, TimeProvider timeProvider) {
        return new DataContainer(filmRepository, customerRepository, rentalRepository, timeProvider);
    }

   /* @Bean
    public CalculationService calculationService(InitialCalculationStrategy newReleaseFilmCalculationStrategy,
                                                 InitialCalculationStrategy regularFilmCalculationStrategy,
                                                 InitialCalculationStrategy oldFilmCalculationStrategy,
                                                 SurchargeCalculationStrategy standardSurchargeCalculationStrategy,
                                                 SurchargeCalculationStrategy extraDaysCalculationStrategy,
                                                 TimeProvider timeProvider){
        return new CalculationService()
    }*/
}
