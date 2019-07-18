package com.casumo.recruitment.videorental.integration.film

import com.casumo.recruitment.videorental.DataContainer
import com.casumo.recruitment.videorental.IntegrationSpec
import com.casumo.recruitment.videorental.film.Film
import com.casumo.recruitment.videorental.film.FilmController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ContextConfiguration(classes = FilmConfiguration.class)
@EnableSpringDataWebSupport
class FilmControllerIntegrationSpec extends IntegrationSpec {

    @Autowired
    private FilmController filmController

    @Autowired
    protected DataContainer dataContainer

    protected MockMvc mockMvc

    def setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(filmController)
                .setControllerAdvice(restControllerAdvice)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .alwaysDo(MockMvcResultHandlers.print())
                .build()

        dataContainer.initializeFilms()
    }

    def 'should get catalogue of films'() {
        when: 'I ask about catalog of films'

        ResultActions filmsResponse = this.mockMvc.perform(get('/api/films')
                .contentType(MediaType.APPLICATION_JSON_UTF8))

        then: 'I get Spider Man and Matrix 11'
        List<Film> listOfFilms = Arrays.asList(
                dataContainer.matrix(),
                dataContainer.spiderMan()
        )

        filmsResponse.andExpect(status().isOk())
                .andExpect(content().json(IntegrationSpec.buildJson(listOfFilms)))
    }

    def 'should get films details'() {
        when: 'I ask about Matrix details'
        def matrix = dataContainer.matrix()
        ResultActions filmsResponse = this.mockMvc.perform(get('/api/films/{id}', matrix.id)
                .contentType(MediaType.APPLICATION_JSON_UTF8))

        then: 'I get details about Matrix'
        filmsResponse.andExpect(status().isOk())
                .andExpect(content().json(IntegrationSpec.buildJson(matrix)))
    }
}
