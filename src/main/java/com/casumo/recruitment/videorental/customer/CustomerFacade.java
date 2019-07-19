package com.casumo.recruitment.videorental.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Component
@Transactional
@AllArgsConstructor
public class CustomerFacade {

    private final CustomerRepository customerRepository;

    public Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(Collections.singletonMap("id", String.valueOf(customerId))));
    }

    public Customer createClient(PersonalData personalData) {
        Optional<Customer> maybeCustomer = customerRepository.findByPersonalDataEmail(personalData.getEmail());

        if (maybeCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException(Collections.singletonMap("email", personalData.getEmail()));
        }

        return customerRepository.save(Customer.builder()
                .personalData(personalData)
                .build());
    }
}
