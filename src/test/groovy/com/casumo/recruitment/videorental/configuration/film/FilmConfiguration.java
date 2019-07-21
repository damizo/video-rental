package com.casumo.recruitment.videorental.configuration.film;

import com.casumo.recruitment.videorental.film.FilmController;
import com.casumo.recruitment.videorental.film.FilmFacade;
import com.casumo.recruitment.videorental.film.FilmMapper;
import com.casumo.recruitment.videorental.film.FilmRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilmConfiguration {

    @Bean
    public FilmFacade filmFacade(FilmRepository filmRepository, FilmMapper filmMapper) {
        return new FilmFacade(filmRepository, filmMapper);
    }

    @Bean
    public FilmController filmController(FilmFacade filmFacade) {
        return new FilmController(filmFacade);
    }

    @Bean
    public FilmRepository filmRepository() {
        return new InMemoryFilmRepository();
    }

    @Bean
    public FilmMapper filmMapper() {
        return new FilmMapper();
    }
}
