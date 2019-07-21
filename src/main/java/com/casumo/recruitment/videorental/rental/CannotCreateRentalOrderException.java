package com.casumo.recruitment.videorental.rental;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class CannotCreateRentalOrderException extends RuntimeException {

    private Map<String, String> params;
}
