package com.casumo.recruitment.videorental.rental;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class FilmAlreadyExistsInBoxException extends RuntimeException {

    private Map<String, String> params;
}
