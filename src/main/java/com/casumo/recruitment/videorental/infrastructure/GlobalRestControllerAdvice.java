package com.casumo.recruitment.videorental.infrastructure;

import com.casumo.recruitment.videorental.customer.CustomerAlreadyExistsException;
import com.casumo.recruitment.videorental.film.FilmNotFoundException;
import com.casumo.recruitment.videorental.film.PriceNotAvailableException;
import com.casumo.recruitment.videorental.film.ReliefNotAvailableException;
import com.casumo.recruitment.videorental.rental.NoCalculationTypeFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public Error handleFilmNotFoundException(FilmNotFoundException e) {
        return Error.builder()
                .message("Film not found")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public Error handleReliefNotAvailableException(ReliefNotAvailableException e) {
        return Error.builder()
                .message("Relief not available in such film type")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public Error handlePriceNotAvailableException(PriceNotAvailableException e) {
        return Error.builder()
                .message("Price not available in such film type")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public Error handleNoCalculationTypeFoundException(NoCalculationTypeFoundException e) {
        return Error.builder()
                .message("No calculation found for such film type")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public Error handleCustomerAlreadyExistsException(CustomerAlreadyExistsException e) {
        return Error.builder()
                .message("Customer wiith particular email already exists")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }
}
