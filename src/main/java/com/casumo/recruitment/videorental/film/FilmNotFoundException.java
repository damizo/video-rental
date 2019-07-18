package com.casumo.recruitment.videorental.film;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class FilmNotFoundException extends RuntimeException {

    private Map<String, String> params = new HashMap<>();

}
