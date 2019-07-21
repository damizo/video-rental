package com.casumo.recruitment.videorental.customer;

import com.casumo.recruitment.videorental.shared.domain.PersonalData;
import com.casumo.recruitment.videorental.shared.dto.PersonalDataDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDTO toDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .personalData(toDTO(customer.getPersonalData()))
                .bonusPoints(customer.getBonusPoints())
                .build();
    }

    private PersonalDataDTO toDTO(PersonalData personalData) {
        return PersonalDataDTO.builder()
                .email(personalData.getEmail())
                .firstName(personalData.getFirstName())
                .lastName(personalData.getLastName())
                .build();
    }
}
