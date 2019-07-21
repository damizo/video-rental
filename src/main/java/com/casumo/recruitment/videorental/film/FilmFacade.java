package com.casumo.recruitment.videorental.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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
                .map(film -> film.checkPrice(numberOfDays))
                .orElseThrow(() -> new FilmNotFoundException(Collections.singletonMap("id", String.valueOf(filmId))));
    }

    public FilmDTO addFilm(FilmDTO filmDTO) {
        validateFilm(filmDTO);
        return Optional.ofNullable(filmMapper.toDomain(filmDTO))
                .map(filmRepository::save)
                .map(filmMapper::toDTO)
                .orElseThrow(() -> new CannotCreateFilmException(
                        "title", filmDTO.getTitle(),
                        "barCode", filmDTO.getBarCode()
                ));
    }

    private void validateFilm(FilmDTO filmDTO) {
        String barCode = filmDTO.getBarCode();
        String title = filmDTO.getTitle();
        String filmType = filmDTO.getType();

        Optional<Film> maybeFilmByBarCode = filmRepository.findByBarCode(barCode);

        if (maybeFilmByBarCode.isPresent()) {
            throw new FilmAlreadyExistsException(Collections.singletonMap("barCode", barCode));
        }

        Optional<Film> maybeFilmByTitle = filmRepository.findByTitle(title);

        if (maybeFilmByTitle.isPresent()) {
            throw new FilmAlreadyExistsException(Collections.singletonMap("title", title));
        }

        Arrays.stream(FilmType.values()).map(Enum::name)
                .filter(name -> name.equalsIgnoreCase(filmType))
                .findAny()
                .orElseThrow(() -> new FilmTypeNotFoundException(Collections.singletonMap("type", filmType)));

    }

}
