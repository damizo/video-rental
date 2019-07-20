package com.casumo.recruitment.videorental.rental;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class RentalOrderNotFoundException extends RuntimeException {

    private Map<String, String> params;

}
