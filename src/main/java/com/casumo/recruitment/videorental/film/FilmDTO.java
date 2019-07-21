package com.casumo.recruitment.videorental.film;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilmDTO {

    private Long id;
    private String barCode;
    private String title;
    private String type;
}
