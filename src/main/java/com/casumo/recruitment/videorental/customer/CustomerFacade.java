package com.casumo.recruitment.videorental.customer;

import com.casumo.recruitment.videorental.shared.dto.PersonalDataDTO;
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
    private final CustomerFactory customerFactory;
    private final CustomerMapper customerMapper;

    public CustomerDTO getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new CustomerNotFoundException(Collections
                        .singletonMap("id", String.valueOf(customerId))));
    }

    public CustomerDTO createCustomer(PersonalDataDTO personalData) {
        Optional<Customer> maybeCustomer = customerRepository.findByPersonalDataEmail(personalData.getEmail());

        if (maybeCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException(Collections.singletonMap("email", personalData.getEmail()));
        }

        return Optional.ofNullable(customerMapper.toDomain(personalData))
                .map(customerFactory::create)
                .map(customerRepository::save)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new CannotCreateCustomerException(Collections
                        .singletonMap("email", personalData.getEmail())));
    }
}
