package com.casumo.recruitment.videorental.configuration.film;

import com.casumo.recruitment.videorental.DataContainer;
import com.casumo.recruitment.videorental.film.FilmController;
import com.casumo.recruitment.videorental.film.FilmFacade;
import com.casumo.recruitment.videorental.film.FilmRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilmConfiguration {

    @Bean
    public FilmFacade filmFinder(FilmRepository filmRepository) {
        return new FilmFacade(filmRepository);
    }

    @Bean
    public FilmController filmController(FilmFacade filmFinder) {
        return new FilmController(filmFinder);
    }

    @Bean
    public FilmRepository filmRepository() {
        return new InMemoryFilmRepository();
    }

    @Bean
    public DataContainer dataContainer(FilmRepository filmRepository){
        return new DataContainer(filmRepository, Mockito.any(), Mockito.any());
    }

}
