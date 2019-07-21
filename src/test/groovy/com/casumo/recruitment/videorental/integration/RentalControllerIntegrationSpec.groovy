package com.casumo.recruitment.videorental.integration

import com.casumo.recruitment.videorental.configuration.database.DatabaseConfiguration
import com.casumo.recruitment.videorental.configuration.rental.RentalConfiguration
import com.casumo.recruitment.videorental.customer.CustomerController
import com.casumo.recruitment.videorental.customer.CustomerRepository
import com.casumo.recruitment.videorental.infrastructure.IntegrationSpec
import com.casumo.recruitment.videorental.rental.RentFilmEntryDTO
import com.casumo.recruitment.videorental.rental.RentalController
import com.casumo.recruitment.videorental.rental.RentalOrderDraftDTO
import com.casumo.recruitment.videorental.shared.time.TimeProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpSession
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ContextConfiguration(classes = [
        RentalConfiguration.class,
        TimeProvider.class,
        DatabaseConfiguration.class
])
@SpringBootTest
class RentalControllerIntegrationSpec extends IntegrationSpec {

    @Autowired
    private RentalController rentalController

    @Autowired
    private CustomerController customerController

    @Autowired
    private CustomerRepository customerRepository

    def setup() {
        dataContainer.initializeCustomers()
        dataContainer.initializeFilms()

        mockMvc = MockMvcBuilders
                .standaloneSetup(rentalController)
                .setControllerAdvice(restControllerAdvice)
                .alwaysDo(MockMvcResultHandlers.print())
                .build()

        httpSession = new MockHttpSession()
    }

    def 'should add film to box'() {
        when: 'I add Matrix to rental box'
        Integer matrixRentalDays = 1
        RentFilmEntryDTO matrixFilm = dataContainer.matrixEntry(matrixRentalDays)

        ResultActions resultAction = this.mockMvc
                .perform(post('/api/rentals/box')
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(buildJson(matrixFilm)))

        then: 'I get rental details with 40 SEK total price'
        httpSession.getId() >> "1"

        RentFilmEntryDTO expectedMatrixEntryWithPrice = matrixFilm.toBuilder()
                .price(BigDecimal.valueOf(40))
                .numberOfDays(matrixRentalDays)
                .build()

        RentalOrderDraftDTO expectedRentalOrderDraft = RentalOrderDraftDTO.builder()
                .films(Arrays.asList(expectedMatrixEntryWithPrice))
                .totalPrice(BigDecimal.valueOf(40))
                .build()

        resultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedRentalOrderDraft)))

    }

    def 'should not add film to box twice'() {
        when: 'I add Matrix to rental box'
        Integer matrixRentalDays = 1
        RentFilmEntryDTO matrixFilm = dataContainer.matrixEntry(matrixRentalDays)

        ResultActions addMatrixToBoxResultAction = this.mockMvc
                .perform(post('/api/rentals/box')
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(buildJson(matrixFilm)))

        then: 'I get rental details with 40 SEK total price'
        httpSession.getId() >> "1"

        RentFilmEntryDTO expectedMatrixEntryWithPrice = matrixFilm.toBuilder()
                .price(BigDecimal.valueOf(40))
                .numberOfDays(matrixRentalDays)
                .build()

        RentalOrderDraftDTO expectedRentalOrderDraft = RentalOrderDraftDTO.builder()
                .films(Arrays.asList(expectedMatrixEntryWithPrice))
                .totalPrice(BigDecimal.valueOf(40))
                .build()

        addMatrixToBoxResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedRentalOrderDraft)))

        when: 'I try to add Matrix second time'
        ResultActions addMatrixToBoxResultActionFailure = this.mockMvc
                .perform(post('/api/rentals/box')
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(buildJson(matrixFilm)))

        then: 'Film cannot be added twice'
        addMatrixToBoxResultActionFailure
                .andExpect(status().is(HttpStatus.EXPECTATION_FAILED.value()))
    }

    def 'should remove film from box'() {
        when: 'I add Matrix to rental box'
        Integer matrixRentalDays = 1
        RentFilmEntryDTO matrixFilm = dataContainer.matrixEntry(matrixRentalDays)

        ResultActions addMatrixToBoxResultAction = this.mockMvc
                .perform(post('/api/rentals/box')
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(buildJson(matrixFilm)))

        then: 'I get rental details with 40 SEK total price'
        httpSession.getId() >> "1"

        RentFilmEntryDTO expectedMatrixEntryWithPrice = matrixFilm.toBuilder()
                .price(BigDecimal.valueOf(40))
                .numberOfDays(matrixRentalDays)
                .build()

        RentalOrderDraftDTO expectedRentalOrderDraft = RentalOrderDraftDTO.builder()
                .films(Arrays.asList(expectedMatrixEntryWithPrice))
                .totalPrice(BigDecimal.valueOf(40))
                .build()

        addMatrixToBoxResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedRentalOrderDraft)))

        when: 'I remove film from box'
        ResultActions removeMatrixFromBoxResultAction = this.mockMvc
                .perform(delete('/api/rentals/box?filmId={filmId}', matrixFilm.getFilmId())
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON))

        then: 'Film removed successfully'
        removeMatrixFromBoxResultAction
                .andExpect(status().isOk())

        when: 'I get box details'
        ResultActions getDetailsResultAction = this.mockMvc
                .perform(get('/api/rentals/box')
                .session(httpSession))

        RentalOrderDraftDTO emptyRentalOrderDraft = expectedRentalOrderDraft.builder()
                .totalPrice(BigDecimal.ZERO)
                .films(Arrays.asList())
                .build()

        then: 'There is not Matrix in box'
        getDetailsResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(emptyRentalOrderDraft)))
    }
}