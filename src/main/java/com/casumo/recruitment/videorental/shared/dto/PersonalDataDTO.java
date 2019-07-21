package com.casumo.recruitment.videorental.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalDataDTO {
    private String firstName;
    private String lastName;
    private String email;
}

