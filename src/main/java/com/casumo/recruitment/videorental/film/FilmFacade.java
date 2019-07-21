package com.casumo.recruitment.videorental.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
@AllArgsConstructor
public class FilmFacade {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public List<FilmDTO> getFilms() {
        return filmRepository.findAll()
                .stream()
                .map(filmMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FilmDTO getFilm(Long filmId) {
        return filmRepository.findById(filmId)
                .map(filmMapper::toDTO)
                .orElseThrow(() -> new FilmNotFoundException(Collections.singletonMap("id", filmId.toString())));
    }

    public BigDecimal checkPrice(Long filmId, Integer numberOfDays) {
        return filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(Collections.singletonMap("id", String.valueOf(filmId))))
                .getType()
                .calculatePrice(numberOfDays);
    }
}
