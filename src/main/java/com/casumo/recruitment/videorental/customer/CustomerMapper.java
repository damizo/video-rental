package com.casumo.recruitment.videorental.customer;

import com.casumo.recruitment.videorental.shared.domain.PersonalData;
import com.casumo.recruitment.videorental.shared.dto.PersonalDataDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerMapper {

    private static final Integer DEFAULT_BONUS_POINTS = 0;

    public CustomerDTO toDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .currency(Optional.ofNullable(customer.getCurrency()).map(Enum::name).orElse(null))
                .personalData(toDTO(customer.getPersonalData()))
                .bonusPoints(Optional.ofNullable(customer.getBonusPoints()).orElse(DEFAULT_BONUS_POINTS))
                .build();
    }

    public PersonalDataDTO toDTO(PersonalData personalData) {
        return PersonalDataDTO.builder()
                .email(personalData.getEmail())
                .firstName(personalData.getFirstName())
                .lastName(personalData.getLastName())
                .build();
    }

    public PersonalData toDomain(PersonalDataDTO personalDataDTO) {
        return PersonalData.builder()
                .email(personalDataDTO.getEmail())
                .firstName(personalDataDTO.getFirstName())
                .lastName(personalDataDTO.getLastName())
                .build();
    }
}
