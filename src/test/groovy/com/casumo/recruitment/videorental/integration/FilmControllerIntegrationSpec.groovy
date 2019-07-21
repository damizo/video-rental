package com.casumo.recruitment.videorental.integration

import com.casumo.recruitment.videorental.configuration.TimeConfiguration
import com.casumo.recruitment.videorental.configuration.database.DatabaseConfiguration
import com.casumo.recruitment.videorental.configuration.film.FilmConfiguration
import com.casumo.recruitment.videorental.film.FilmController
import com.casumo.recruitment.videorental.film.FilmDTO
import com.casumo.recruitment.videorental.infrastructure.IntegrationSpec
import com.casumo.recruitment.videorental.infrastructure.Profiles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Profile(Profiles.TEST)
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

    def cleanup() {
        dataContainer.cleanUp()
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

    def 'should not add film with the same title'() {
        given: 'Matrix 11'
        FilmDTO matrixDTO = dataContainer.matrixDTO()

        when: 'I want to add Matrix 11'
        ResultActions addFilmResultAction = this.mockMvc
                .perform(post('/api/films')
                .content(buildJson(matrixDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then: 'Matrix is already in inventory'
        addFilmResultAction.andExpect(status().is4xxClientError())
    }

    def 'should not add film with the same barcode'() {
        given: 'Matrix 11'
        FilmDTO matrixDTO = dataContainer.matrixDTO()

        matrixDTO.toBuilder()
                .title('Matrix 12')
                .build()

        when: 'I want to add Matrix 12'
        ResultActions addFilmResultAction = this.mockMvc
                .perform(post('/api/films')
                .content(buildJson(matrixDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then: 'There is already film in inventory with the same barcode'
        addFilmResultAction.andExpect(status().is4xxClientError())
    }

    def 'should not add film with unknown film type'() {
        given: 'Godfather'
        FilmDTO godfatherDTO = dataContainer.newGodfatherDTO()

        godfatherDTO = godfatherDTO.toBuilder()
                .type('AMAZING_MOVIE')
                .build()

        when: 'I want to add Godfather'
        ResultActions addFilmResultAction = this.mockMvc
                .perform(post('/api/films')
                .content(buildJson(godfatherDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then: 'Film type not found'
        addFilmResultAction.andExpect(status().is4xxClientError())
    }

    def 'should add film'() {
        given: 'Godfather'
        FilmDTO godfatherDTO = dataContainer.newGodfatherDTO()

        when: 'I want to add Godfather'
        ResultActions addFilmResultAction = this.mockMvc
                .perform(post('/api/films')
                .content(buildJson(godfatherDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then: 'Godfather has been successfully added'
        addFilmResultAction.andExpect(status().isOk())

        and: 'Godfather is in inventory'
        Long newGodfatherId = 5L

        this.mockMvc
                .perform(get('/api/films/{id}', newGodfatherId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(godfatherDTO
                .toBuilder()
                .id(newGodfatherId)
                .build())))
    }
}
