package com.casumo.recruitment.videorental.integration.film;

import com.casumo.recruitment.videorental.film.FilmController;
import com.casumo.recruitment.videorental.film.FilmFinder;
import com.casumo.recruitment.videorental.film.FilmRepository;
import com.casumo.recruitment.videorental.DataContainer;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilmConfiguration {

    @Bean
    public FilmFinder filmFinder(FilmRepository filmRepository) {
        return new FilmFinder(filmRepository);
    }

    @Bean
    public FilmController filmResource(FilmFinder filmFinder) {
        return new FilmController(filmFinder);
    }

    @Bean
    public FilmRepository filmRepository() {
        return new InMemoryFilmRepository();
    }

    @Bean
    public DataContainer dataContainer(FilmRepository filmRepository){
        return new DataContainer(filmRepository, Mockito.any(), Mockito.any(), Mockito.any());
    }

}
