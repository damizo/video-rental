package com.casumo.recruitment.videorental.film;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Film {

    @Id
    private Long id;
    private String barCode;
    private String title;
    private FilmType type;
}
