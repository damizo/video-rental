package com.casumo.recruitment.videorental.customer;

import com.casumo.recruitment.videorental.shared.domain.PersonalData;
import org.springframework.stereotype.Component;

@Component
public class CustomerFactory {

    public Customer create(PersonalData personalData) {
        return Customer.builder()
                .personalData(personalData)
                .build();
    }
}
