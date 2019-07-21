package com.casumo.recruitment.videorental.film;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/films")
@AllArgsConstructor
public class FilmController implements FilmSwaggerDocumentation {

    private final FilmFacade filmFacade;

    @GetMapping
    public List<FilmDTO> getFilms() {
        return filmFacade.getFilms();
    }

    @GetMapping("/{filmId}")
    public FilmDTO getFilm(@PathVariable Long filmId) {
        return filmFacade.getFilm(filmId);
    }

}
