package com.casumo.recruitment.videorental.customer;

import lombok.Data;

import java.util.Map;

@Data
public class CustomerAlreadyExistsException extends RuntimeException {
    private Map<String, String> params;

    public CustomerAlreadyExistsException(Map<String, String> params) {
        this.params = params;
    }
}
