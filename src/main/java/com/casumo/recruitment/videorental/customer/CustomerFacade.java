package com.casumo.recruitment.videorental.customer;

import com.casumo.recruitment.videorental.customer.exception.CustomerAlreadyExistsException;
import com.casumo.recruitment.videorental.customer.exception.CustomerNotFoundException;
import com.casumo.recruitment.videorental.shared.domain.PersonalData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
@Transactional
@AllArgsConstructor
public class CustomerFacade {

    private final CustomerRepository customerRepository;
    private final CustomerFactory customerFactory;
    private final CustomerMapper customerMapper;

    public CustomerDTO getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new CustomerNotFoundException(Collections.singletonMap("id", String.valueOf(customerId))));
    }

    public CustomerDTO createCustomer(PersonalData personalData) {
        customerRepository.findByPersonalDataEmail(personalData.getEmail())
                .ifPresent(customer -> new CustomerAlreadyExistsException(Collections.singletonMap("email", personalData.getEmail())));

        Customer customer = customerFactory.create(personalData);
        Customer newCustomer = customerRepository.save(customer);
        return customerMapper.toDTO(newCustomer);
    }
}
