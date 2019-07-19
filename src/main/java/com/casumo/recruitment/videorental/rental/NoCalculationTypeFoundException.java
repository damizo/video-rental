package com.casumo.recruitment.videorental.rental;

import com.google.common.collect.ImmutableMap;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class NoCalculationTypeFoundException extends RuntimeException {

    private Map<String, String> params = new HashMap<>();

    public NoCalculationTypeFoundException(ImmutableMap<String, String> params) {
        this.params = params;
    }
}

