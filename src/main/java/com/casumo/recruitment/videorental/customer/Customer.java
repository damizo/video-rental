package com.casumo.recruitment.videorental.customer;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Builder
@Data
@Entity
public class Customer {

    @Id
    private Long id;

    private Integer bonusPoints;

    @Embedded
    private PersonalData personalData;

    public void increaseBonusPoints() {
        this.bonusPoints++;
    }

}
