package com.casumo.recruitment.videorental.film;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FilmDTO {

    private Long id;
    private String barCode;
    private String title;
    private String type;
}
