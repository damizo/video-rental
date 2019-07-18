package com.casumo.recruitment.videorental.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
@Transactional(readOnly = true)
@AllArgsConstructor
public class CustomerFinder {

    private final CustomerRepository customerRepository;

    public Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(Collections.singletonMap("id", String.valueOf(customerId))));
    }
}
