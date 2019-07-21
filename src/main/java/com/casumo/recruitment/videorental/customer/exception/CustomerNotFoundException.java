package com.casumo.recruitment.videorental.customer.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class CustomerNotFoundException extends RuntimeException {

    private Map<String, String> params = new HashMap<>();

}
