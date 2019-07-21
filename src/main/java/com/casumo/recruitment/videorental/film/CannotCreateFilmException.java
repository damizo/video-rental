package com.casumo.recruitment.videorental.film;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class CannotCreateFilmException extends RuntimeException {
    private Map<String, String> params;

    public CannotCreateFilmException(String firstKey, String firstValue,
                                     String secondKey, String secondValue){
        this.params.put(firstKey, firstValue);
        this.params.put(secondKey, secondValue);
    }
}
