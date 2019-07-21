package com.casumo.recruitment.videorental.customer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class CannotCreateCustomerException extends RuntimeException {

    private Map<String, String> params;
}
