package com.casumo.recruitment.videorental.customer;

import com.casumo.recruitment.videorental.shared.CurrencyType;
import com.casumo.recruitment.videorental.shared.domain.PersonalData;
import org.springframework.stereotype.Component;

@Component
public class CustomerFactory {

    private static final Integer DEFAULT_BONUS_POINTS = 0;

    public Customer create(PersonalData personalData) {
        return Customer.builder()
                .bonusPoints(DEFAULT_BONUS_POINTS)
                .currency(CurrencyType.SEK)
                .personalData(personalData)
                .build();
    }
}
