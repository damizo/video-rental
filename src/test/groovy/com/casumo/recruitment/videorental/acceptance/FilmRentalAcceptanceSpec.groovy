package com.casumo.recruitment.videorental.acceptance

import com.casumo.recruitment.videorental.IntegrationSpec
import com.casumo.recruitment.videorental.configuration.TimeConfiguration
import com.casumo.recruitment.videorental.configuration.customer.CustomerConfiguration
import com.casumo.recruitment.videorental.configuration.database.DatabaseConfiguration
import com.casumo.recruitment.videorental.configuration.rental.RentalConfiguration
import com.casumo.recruitment.videorental.customer.Customer
import com.casumo.recruitment.videorental.customer.CustomerController
import com.casumo.recruitment.videorental.customer.CustomerRepository
import com.casumo.recruitment.videorental.film.FilmType
import com.casumo.recruitment.videorental.infrastructure.DataContainer
import com.casumo.recruitment.videorental.rental.*
import com.casumo.recruitment.videorental.shared.time.TimeProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpSession
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ContextConfiguration(classes = [RentalConfiguration.class,
        TimeConfiguration.class,
        DatabaseConfiguration.class,
        CustomerConfiguration.class])
class FilmRentalAcceptanceSpec extends IntegrationSpec {

    @Autowired
    private DataContainer dataContainer

    @Autowired
    private RentalController rentalController

    @Autowired
    private CustomerController customerController

    @Autowired
    private CustomerRepository customerRepository

    @Autowired
    private TimeProvider timeProvider

    @Autowired
    private MockHttpSession httpSession

    protected MockMvc mockMvc

    def setup() {
        dataContainer.initializeCustomers()
        dataContainer.initializeFilms()

        mockMvc = MockMvcBuilders
                .standaloneSetup(rentalController, customerController)
                .setMessageConverters(mappingJackson2HttpMessageConverter)
                .alwaysDo(MockMvcResultHandlers.print())
                .build()
    }

    def cleanup() {
        dataContainer.cleanUp()
    }

    def 'should rent new release film for one day and return lately'() {
        setup:
        Customer customer = dataContainer.customer()
        Long customerId = 1L
        RentFilmEntryDTO matrixFilm = dataContainer.matrixEntry(1)

        when: 'I add Matrix to rental box'
        ResultActions resultAction = this.mockMvc
                .perform(post('/api/rental/box')
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(buildJson(matrixFilm)))

        then: 'I get rental details with 40 SEK total price'
        httpSession.getId() >> "1"
        RentFilmEntryDTO rentFilmEntryWithPrice = matrixFilm.toBuilder()
                .price(BigDecimal.valueOf(40))
                .numberOfDays(1)
                .build()

        RentOrderDraftDTO rentalOrderDraft = RentOrderDraftDTO.builder()
                .films(Arrays.asList(rentFilmEntryWithPrice))
                .totalPrice(BigDecimal.valueOf(40))
                .build()

        resultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(rentalOrderDraft)))

        when: 'I complete rent'
        httpSession.getId() >> "1"
        ResultActions completeRentResultAction = this.mockMvc
                .perform(post('/api/rental/rent?customerId={customerId}', String.valueOf(customerId))
                .session(httpSession))

        then: 'I have to pay 40 SEK for order'
        Long rentalOrderId = 1L
        Long firstRentalId = 1L

        BigDecimal expectedTotalPrice = BigDecimal.valueOf(40)
        BigDecimal expectedRentalPrice = expectedTotalPrice

        RentalDTO expectedRental = RentalDTO.builder()
                .id(firstRentalId)
                .customerId(customerId)
                .filmType(FilmType.NEW_RELEASE.name())
                .status(RentalStatus.STARTED.name())
                .surcharge(BigDecimal.ZERO)
                .price(expectedRentalPrice)
                .expectedReturnDate(todayPlus(1))
                .rentDate(todayPlus(0))
                .build()

        RentOrderDTO expectedRentalOrder = RentOrderDTO.builder()
                .id(rentalOrderId)
                .totalPrice(expectedTotalPrice)
                .totalSurcharge(BigDecimal.ZERO)
                .rentals(Arrays.asList(expectedRental))
                .build()

        completeRentResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedRentalOrder)))

        and: 'I have added 2 bonus point'
        customer.increaseBonusPoints(2)

        Customer actualCustomer = customerRepository.findById(customerId).get()
        actualCustomer.bonusPoints == customer.bonusPoints

        when: 'I return Matrix with 2 days of delay'
        ResultActions returnResultAction = this.mockMvc
                .perform(post('/api/rental/return')
                .param('rentalId', String.valueOf(1L)))

        then: 'Rent has ben ended and surcharge increased to 80 SEK'
        timeProvider.today() >> todayPlus(3)

        BigDecimal expectedSurcharge = BigDecimal.valueOf(80)

        RentalDTO expectedRentalWithIncreasedSurcharge = expectedRental.toBuilder()
                .surcharge(expectedSurcharge)
                .status(RentalStatus.END.name())
                .actualReturnDate(todayPlus(3))
                .build()

        returnResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedRentalWithIncreasedSurcharge)))
    }

    def 'should rent regular and old film and return lately'() {
        setup:
        Customer customer = dataContainer.customer()
        Long customerId = 1L
        Integer spiderManRentalDays = 5
        RentFilmEntryDTO spiderManFilm = dataContainer.spiderManEntry(spiderManRentalDays)

        when: 'I add Spider Man to rental box'
        ResultActions spiderManAddedToBoxResultAction = this.mockMvc
                .perform(post('/api/rental/box')
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(buildJson(spiderManFilm)))

        then: 'I get rental details with 90 SEK total price'
        httpSession.getId() >> "1"

        BigDecimal expectedRentalPriceOfSpiderMan = BigDecimal.valueOf(90)

        RentFilmEntryDTO spiderManWithPrice = spiderManFilm.toBuilder()
                .price(expectedRentalPriceOfSpiderMan)
                .numberOfDays(spiderManRentalDays)
                .build()

        RentOrderDraftDTO rentalOrderDraft = RentOrderDraftDTO.builder()
                .films(Arrays.asList(spiderManWithPrice))
                .totalPrice(expectedRentalPriceOfSpiderMan)
                .build()

        spiderManAddedToBoxResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(rentalOrderDraft)))

        when: 'I add Out of Africa to rental box'
        Integer outOfAfricaRentalDays = 7
        RentFilmEntryDTO outOfAfricaFilm = dataContainer.outOfAfricaEntry(outOfAfricaRentalDays)

        ResultActions outOfAfricaAddedToBoxResultAction = this.mockMvc
                .perform(post('/api/rental/box')
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(buildJson(outOfAfricaFilm)))

        then: 'I get rental details with 180 SEK total price'
        httpSession.getId() >> "1"

        BigDecimal outOfAfricaRentalPrice = BigDecimal.valueOf(90)
        BigDecimal totalPriceOfRental = BigDecimal.valueOf(180)

        RentFilmEntryDTO outOfAfricaWithPrice = outOfAfricaFilm.toBuilder()
                .price(outOfAfricaRentalPrice)
                .numberOfDays(outOfAfricaRentalDays)
                .build()


        RentOrderDraftDTO finalRentalOrderDraft = RentOrderDraftDTO.builder()
                .films(Arrays.asList(spiderManWithPrice, outOfAfricaWithPrice))
                .totalPrice(totalPriceOfRental)
                .build()


        outOfAfricaAddedToBoxResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(finalRentalOrderDraft)))

        when: 'I complete rent'
        httpSession.getId() >> "1"
        ResultActions completeRentResultAction = this.mockMvc
                .perform(post('/api/rental/rent?customerId={customerId}', String.valueOf(customerId))
                .session(httpSession))

        then: 'I have to pay 180 SEK for order'
        Long rentalOrderId = 1L
        Long firstRentalId = 1L
        Long secondRentalId = 2L
        BigDecimal expectedTotalPrice = BigDecimal.valueOf(180)

        RentalDTO spiderManRental = RentalDTO.builder()
                .id(firstRentalId)
                .customerId(customerId)
                .filmType(FilmType.REGULAR.name())
                .status(RentalStatus.STARTED.name())
                .surcharge(BigDecimal.ZERO)
                .price(expectedRentalPriceOfSpiderMan)
                .expectedReturnDate(todayPlus(spiderManRentalDays))
                .rentDate(todayPlus(0))
                .build()

        RentalDTO outOfAfricaRental = RentalDTO.builder()
                .id(secondRentalId)
                .customerId(customerId)
                .filmType(FilmType.OLD.name())
                .status(RentalStatus.STARTED.name())
                .surcharge(BigDecimal.ZERO)
                .price(outOfAfricaRentalPrice)
                .expectedReturnDate(todayPlus(outOfAfricaRentalDays))
                .rentDate(todayPlus(0))
                .build()

        RentOrderDTO expectedRentalOrder = RentOrderDTO.builder()
                .id(rentalOrderId)
                .totalPrice(expectedTotalPrice)
                .totalSurcharge(BigDecimal.ZERO)
                .rentals(Arrays.asList(spiderManRental, outOfAfricaRental))
                .build()

        completeRentResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedRentalOrder)))

        and: 'I have added 2 bonus points'
        customer.increaseBonusPoints(2)

        Customer actualCustomer = customerRepository.findById(customerId).get()
        actualCustomer.bonusPoints == customer.bonusPoints

        when: 'I return Spider Man with 4 days of delay'
        Long spiderManRentalId = 1L
        ResultActions returnSpiderMan = this.mockMvc
                .perform(post('/api/rental/return')
                .param('rentalId', String.valueOf(spiderManRentalId)))

        then: 'Rental of Spider Man has been ended and surcharge increased to 120 SEK'
        timeProvider.today() >> todayPlus(9)

        BigDecimal expectedSurcharge = BigDecimal.valueOf(120)

        RentalDTO spiderManRentalAfterReturn = spiderManRental.toBuilder()
                .surcharge(expectedSurcharge)
                .status(RentalStatus.END.name())
                .actualReturnDate(todayPlus(9))
                .build()

        returnSpiderMan
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(spiderManRentalAfterReturn)))

        when: 'I return Out of Africa with 2 days of delay'
        Long outOfAfricaRentalId = 2L
        ResultActions returnOutOfAfrica = this.mockMvc
                .perform(post('/api/rental/return')
                .param('rentalId', String.valueOf(outOfAfricaRentalId)))

        then: 'Rental of Out of Africa has been ended and surcharge increased to 60 SEK'
        timeProvider.today() >> todayPlus(9)

        BigDecimal outOfAfricaSurcharge = BigDecimal.valueOf(60)

        RentalDTO outOfAfricaAfterReturn = outOfAfricaRental.toBuilder()
                .surcharge(outOfAfricaSurcharge)
                .status(RentalStatus.END.name())
                .actualReturnDate(todayPlus(9))
                .build()

        returnOutOfAfrica
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(outOfAfricaAfterReturn)))

        when: 'I get details about rental order'
        BigDecimal finalSurcharge = BigDecimal.valueOf(180)
        expectedRentalOrder = expectedRentalOrder.toBuilder()
                .rentals(Arrays.asList(outOfAfricaAfterReturn, spiderManRentalAfterReturn))
                .totalSurcharge(finalSurcharge)
                .build()

        ResultActions getRentalOrderDetailsResultAction = this.mockMvc
                .perform(get('/api/rental/rent/{rentalOrderId}', rentalOrderId))

        then: 'Final surcharge is 180 SEK'
        getRentalOrderDetailsResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedRentalOrder)))
    }


    def 'should rent regular film and return before end of first days relief'() {
        setup:
        Customer customer = dataContainer.customer()
        Long customerId = 1L
        Integer numberOfRentalDays = 1
        RentFilmEntryDTO spiderManFilm = dataContainer.spiderManEntry(numberOfRentalDays)

        when: 'I add Spider Man to rental box'
        ResultActions resultAction = this.mockMvc
                .perform(post('/api/rental/box')
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(buildJson(spiderManFilm)))

        then: 'I get rental details with 40 SEK total price'
        httpSession.getId() >> "1"

        BigDecimal spiderManPrice = BigDecimal.valueOf(30)

        RentFilmEntryDTO rentFilmEntryWithPrice = spiderManFilm.toBuilder()
                .price(spiderManPrice)
                .numberOfDays(numberOfRentalDays)
                .build()

        RentOrderDraftDTO rentalOrderDraft = RentOrderDraftDTO.builder()
                .films(Arrays.asList(rentFilmEntryWithPrice))
                .totalPrice(spiderManPrice)
                .build()

        resultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(rentalOrderDraft)))

        when: 'I complete rent'
        httpSession.getId() >> "1"

        ResultActions completeRentResultAction = this.mockMvc
                .perform(post('/api/rental/rent?customerId={customerId}', String.valueOf(customerId))
                .session(httpSession))

        and: 'I have added 1 bonus point'
        customer.increaseBonusPoints(1)

        Customer actualCustomer = customerRepository.findById(customerId).get()
        actualCustomer.bonusPoints == customer.bonusPoints


        then: 'I have to pay 30 SEK for order'
        Long rentalOrderId = 1L
        Long firstRentalId = 1L
        BigDecimal expectedTotalPrice = BigDecimal.valueOf(spiderManPrice)

        RentalDTO spiderManRental = RentalDTO.builder()
                .id(firstRentalId)
                .customerId(customerId)
                .filmType(FilmType.REGULAR.name())
                .status(RentalStatus.STARTED.name())
                .surcharge(BigDecimal.ZERO)
                .price(expectedTotalPrice)
                .expectedReturnDate(todayPlus(1))
                .rentDate(todayPlus(0))
                .build()

        RentOrderDTO expectedRentalOrder = RentOrderDTO.builder()
                .id(rentalOrderId)
                .totalPrice(expectedTotalPrice)
                .totalSurcharge(BigDecimal.ZERO)
                .rentals(Arrays.asList(spiderManRental))
                .build()

        completeRentResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedRentalOrder)))

        when: 'I return Spider Man day before end of first 3 days of relief'
        Long spiderManRentalId = 1L
        ResultActions returnSpiderMan = this.mockMvc
                .perform(post('/api/rental/return')
                .param('rentalId', String.valueOf(spiderManRentalId)))

        then: 'Rental of Spider Man has been ended without surcharge'
        timeProvider.today() >> todayPlus(1)

        RentalDTO spiderManRentalAfterReturn = spiderManRental.toBuilder()
                .surcharge(BigDecimal.valueOf(0))
                .status(RentalStatus.END.name())
                .actualReturnDate(todayPlus(1))
                .build()

        returnSpiderMan
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(spiderManRentalAfterReturn)))

        when: 'I get details about rental order'
        BigDecimal finalSurcharge = BigDecimal.valueOf(0)
        expectedRentalOrder = expectedRentalOrder.toBuilder()
                .rentals(Arrays.asList(spiderManRentalAfterReturn))
                .totalSurcharge(finalSurcharge)
                .build()

        ResultActions getRentalOrderDetailsResultAction = this.mockMvc
                .perform(get('/api/rental/rent/{rentalOrderId}', rentalOrderId))

        then: 'Final surcharge is 0 SEK'
        getRentalOrderDetailsResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedRentalOrder)))
    }

    LocalDate todayPlus(Integer days) {
        return LocalDate.now().plusDays(days)
    }

    LocalDate todayMinus(Integer days) {
        return LocalDate.now().minusDays(days)
    }
}
