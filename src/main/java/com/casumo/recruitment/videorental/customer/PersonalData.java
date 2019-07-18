package com.casumo.recruitment.videorental.customer;

import lombok.Builder;

import javax.persistence.Embeddable;

@Embeddable
@Builder
public class PersonalData {
    private String firstName;
    private String lastName;
    private String email;
}
