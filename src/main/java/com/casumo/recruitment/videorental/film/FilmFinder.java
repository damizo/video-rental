package com.casumo.recruitment.videorental.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Component
@Transactional(readOnly = true)
@AllArgsConstructor
public class FilmFinder {

    private final FilmRepository filmRepository;

    public List<Film> getFilms() {
        return filmRepository.findAll();
    }

    public Film getFilm(Long filmId) {
        return filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(Collections.singletonMap("id", filmId.toString())));
    }
}
