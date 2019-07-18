package com.casumo.recruitment.videorental.rental;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RentalNotFoundException extends RuntimeException{
}
