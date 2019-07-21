package com.casumo.recruitment.videorental.film;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class FilmTypeNotFoundException extends RuntimeException {

    private Map<String, String> params;

}
