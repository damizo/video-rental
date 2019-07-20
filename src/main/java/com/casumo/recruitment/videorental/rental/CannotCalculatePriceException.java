package com.casumo.recruitment.videorental.rental;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class CannotCalculatePriceException extends RuntimeException {

    private Map<String, String> params;

}
