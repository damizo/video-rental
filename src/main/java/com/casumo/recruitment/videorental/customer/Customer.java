package com.casumo.recruitment.videorental.customer;

import com.casumo.recruitment.videorental.shared.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {

    @Id
    private Long id;

    private Integer bonusPoints = 0;

    @Embedded
    private PersonalData personalData;

    private CurrencyType currency = CurrencyType.SEK;

    public void increaseBonusPoints(Integer bonusPoints) {
        this.bonusPoints += bonusPoints;
    }

    public String getEmail() {
        return this.personalData.getEmail();
    }

}
