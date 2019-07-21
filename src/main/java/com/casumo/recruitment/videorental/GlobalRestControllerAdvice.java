package com.casumo.recruitment.videorental;

import com.casumo.recruitment.videorental.customer.CannotCreateCustomerException;
import com.casumo.recruitment.videorental.customer.CustomerAlreadyExistsException;
import com.casumo.recruitment.videorental.customer.CustomerNotFoundException;
import com.casumo.recruitment.videorental.film.FilmAlreadyExistsException;
import com.casumo.recruitment.videorental.film.FilmNotFoundException;
import com.casumo.recruitment.videorental.film.FilmTypeNotFoundException;
import com.casumo.recruitment.videorental.infrastructure.exception.ErrorDTO;
import com.casumo.recruitment.videorental.rental.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

@RestControllerAdvice
public class GlobalRestControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FilmNotFoundException.class)
    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public ErrorDTO handleFilmNotFoundException(FilmNotFoundException e) {
        return ErrorDTO.builder()
                .message("Film not found")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public ErrorDTO handleCustomerNotFound(CustomerNotFoundException e) {
        return ErrorDTO.builder()
                .message("Customer not found")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ExceptionHandler(RentalNotFoundException.class)
    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public ErrorDTO handleRentalNotFoundException(RentalNotFoundException e) {
        return ErrorDTO.builder()
                .message("Rental not found")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ExceptionHandler(RentalOrderNotFoundException.class)
    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public ErrorDTO handleRentalOrderNotFoundException(RentalOrderNotFoundException e) {
        return ErrorDTO.builder()
                .message("Rental order not found")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ExceptionHandler(NoCalculationTypeFoundException.class)
    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public ErrorDTO handleNoCalculationTypeFoundException(NoCalculationTypeFoundException e) {
        return ErrorDTO.builder()
                .message("No calculation found for such film type")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public ErrorDTO handleCustomerAlreadyExistsException(CustomerAlreadyExistsException e) {
        return ErrorDTO.builder()
                .message("Customer wiith particular email already exists")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ExceptionHandler(NoSuchFilmInBoxException.class)
    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public ErrorDTO handleNoSuchFilmInBoxException(NoSuchFilmInBoxException e) {
        return ErrorDTO.builder()
                .message("Such film not found in rental box")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ExceptionHandler(FilmAlreadyExistsInBoxException.class)
    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public ErrorDTO handleFilmAlreadyExistsInBoxException(FilmAlreadyExistsInBoxException e) {
        return ErrorDTO.builder()
                .message("Film already exists in box")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ExceptionHandler(FilmAlreadyExistsException.class)
    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public ErrorDTO handleFilmAlreadyExistsException(FilmAlreadyExistsException e) {
        return ErrorDTO.builder()
                .message("Film already exists in database")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ExceptionHandler(FilmTypeNotFoundException.class)
    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public ErrorDTO handleFilmTypeNotFoundException(FilmTypeNotFoundException e) {
        return ErrorDTO.builder()
                .message("Film type not found")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ExceptionHandler(CannotCalculatePriceException.class)
    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public ErrorDTO handleCannotCalculatePriceException(CannotCalculatePriceException e) {
        return ErrorDTO.builder()
                .message("Cannot calculate price")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ExceptionHandler(CannotCreateCustomerException.class)
    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public ErrorDTO handleCannotCreateCustomerException(CannotCreateCustomerException e) {
        return ErrorDTO.builder()
                .message("Cannot create customer")
                .params(Collections.unmodifiableMap(e.getParams()))
                .build();
    }

    @ExceptionHandler(CannotCreateRentalOrderException.class)
    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public ErrorDTO handleCannotCreateRentalOrderException(CannotCreateRentalOrderException e) {
        return ErrorDTO.builder()
                .message("Cannot create rental order")
                .params(Collections.emptyMap())
                .build();
    }

}
