package com.casumo.recruitment.videorental.integration

import com.casumo.recruitment.videorental.configuration.database.DatabaseConfiguration
import com.casumo.recruitment.videorental.configuration.film.FilmConfiguration
import com.casumo.recruitment.videorental.film.Film
import com.casumo.recruitment.videorental.film.FilmController
import com.casumo.recruitment.videorental.film.FilmDTO
import com.casumo.recruitment.videorental.infrastructure.IntegrationSpec
import com.casumo.recruitment.videorental.configuration.TimeConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ContextConfiguration(classes = [
        FilmConfiguration.class,
        TimeConfiguration.class,
        DatabaseConfiguration.class
])
class FilmControllerIntegrationSpec extends IntegrationSpec {

    @Autowired
    private FilmController filmController

    def setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(filmController)
                .setControllerAdvice(restControllerAdvice)
                .alwaysDo(MockMvcResultHandlers.print())
                .build()

        dataContainer.initializeFilms()
    }

    def 'should get catalogue of films'() {
        when: 'I ask about catalogue of films'

        ResultActions filmsResponse = this.mockMvc
                .perform(get('/api/films')
                .contentType(MediaType.APPLICATION_JSON))

        then: 'I get whole catalogue of films'
        List<FilmDTO> listOfFilms = Arrays.asList(
                dataContainer.matrixDTO(),
                dataContainer.spiderManDTO(),
                dataContainer.spiderManBetterOneDTO(),
                dataContainer.outOfAfricaDTO()
        )

        filmsResponse
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(listOfFilms)))
    }

    def 'should get films details'() {
        when: 'I ask about Out of Africa details'
        FilmDTO outOfAfrica = dataContainer.outOfAfricaDTO()
        ResultActions getFilmsResultAction = this.mockMvc
                .perform(get('/api/films/{id}', outOfAfrica.getId())
                .contentType(MediaType.APPLICATION_JSON))

        then: 'I get details about Matrix'
        getFilmsResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(outOfAfrica)))
    }
}
