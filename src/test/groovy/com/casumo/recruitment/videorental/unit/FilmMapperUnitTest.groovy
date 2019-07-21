package com.casumo.recruitment.videorental.unit

import com.casumo.recruitment.videorental.film.Film
import com.casumo.recruitment.videorental.film.FilmMapper
import com.casumo.recruitment.videorental.film.FilmType
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class FilmMapperUnitTest extends Specification {

    @Autowired
    private FilmMapper filmMapper


    def 'should map domain model to dto'() {
        def domain = new Film(1L, "103001", "Spider Man", FilmType.REGULAR)
        def actualDTO = filmMapper.toDTO(domain)

        actualDTO.id == domain.id
        actualDTO.barCode == domain.barCode
        actualDTO.title == domain.title
        actualDTO.type == domain.type
    }

}
