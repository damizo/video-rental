package com.casumo.recruitment.videorental.film;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FilmMapper {

    public FilmDTO toDTO(Film film) {
        return FilmDTO.builder()
                .id(film.getId())
                .barCode(film.getBarCode())
                .title(film.getTitle())
                .type(Optional.ofNullable(film.getType()).map(Enum::name).orElse(null))
                .build();
    }

    public Film toDomain(FilmDTO film) {
        return Film.builder()
                .id(film.getId())
                .barCode(film.getBarCode())
                .title(film.getTitle())
                .type(Optional.of(FilmType.valueOf(film.getType().toUpperCase())).orElse(null))
                .build();
    }
}
