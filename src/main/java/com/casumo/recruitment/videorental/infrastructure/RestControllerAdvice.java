package com.casumo.recruitment.videorental.infrastructure;

import com.casumo.recruitment.videorental.film.FilmNotFoundException;
import com.casumo.recruitment.videorental.film.ReliefNotAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;

@ControllerAdvice
public class RestControllerAdvice {

    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public Error handleFilmNotFoundException(FilmNotFoundException e) {
        return Error.builder()
                .message(e.getMessage())
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public Error handleReliefNotAvailableException(ReliefNotAvailableException e) {
        return Error.builder()
                .message(e.getMessage())
                .build();
    }

}
