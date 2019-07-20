package com.casumo.recruitment.videorental.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Component
@Transactional
@AllArgsConstructor
public class FilmFacade {

    private final FilmRepository filmRepository;

    public List<Film> getFilms() {
        return filmRepository.findAll();
    }

    public Film getFilm(Long filmId) {
        return filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(Collections.singletonMap("id", filmId.toString())));
    }

    public BigDecimal checkPrice(Long filmId, Integer numberOfDays) {
        return filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(Collections.singletonMap("id", String.valueOf(filmId))))
                .getType()
                .calculatePrice(numberOfDays);
    }
}
