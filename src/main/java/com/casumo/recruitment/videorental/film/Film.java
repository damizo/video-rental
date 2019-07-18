package com.casumo.recruitment.videorental.film;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@Entity
public class Film {

    @Id
    private Long id;
    private String barCode;
    private String title;
    private FilmType type;
}
