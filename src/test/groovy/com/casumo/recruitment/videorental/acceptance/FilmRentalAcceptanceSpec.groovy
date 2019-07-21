package com.casumo.recruitment.videorental.acceptance

import com.casumo.recruitment.videorental.configuration.customer.CustomerConfiguration
import com.casumo.recruitment.videorental.configuration.database.DatabaseConfiguration
import com.casumo.recruitment.videorental.configuration.rental.RentalConfiguration
import com.casumo.recruitment.videorental.customer.Customer
import com.casumo.recruitment.videorental.customer.CustomerController
import com.casumo.recruitment.videorental.customer.CustomerRepository
import com.casumo.recruitment.videorental.film.FilmType
import com.casumo.recruitment.videorental.infrastructure.IntegrationSpec
import com.casumo.recruitment.videorental.configuration.TimeConfiguration
import com.casumo.recruitment.videorental.rental.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
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
    private RentalController rentalController

    @Autowired
    private CustomerController customerController

    @Autowired
    private CustomerRepository customerRepository

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
        given: 'Customer and Matrix entry'
        Customer customer = dataContainer.customer()
        Long customerId = 1L
        Integer matrixRentalDays = 1
        RentFilmEntryDTO matrixFilm = dataContainer.matrixEntry(matrixRentalDays)

        when: 'I add Matrix to rental box'
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

        when: 'I complete rent'
        httpSession.getId() >> "1"
        ResultActions completeRentResultAction = this.mockMvc
                .perform(post('/api/rentals?customerId={customerId}', String.valueOf(customerId))
                .session(httpSession))

        then: 'I have to pay 40 SEK for order'
        Long rentalOrderId = 1L
        Long firstRentalId = rentalOrderId

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
                .rentDate(today())
                .build()

        RentalOrderDTO expectedRentalOrder = RentalOrderDTO.builder()
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
                .perform(post('/api/rentals/returns')
                .param('rentalId', String.valueOf(firstRentalId)))

        BigDecimal expectedSurcharge = BigDecimal.valueOf(80)

        RentalDTO expectedRentalWithIncreasedSurcharge = expectedRental.toBuilder()
                .surcharge(expectedSurcharge)
                .status(RentalStatus.END.name())
                .actualReturnDate(todayPlus(3))
                .build()

        then: 'Rent has ben ended and surcharge increased to 80 SEK'
        timeProvider.today() >> todayPlus(3)

        returnResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedRentalWithIncreasedSurcharge)))
    }

    def 'should rent regular and old film and return lately'() {
        given: 'Customer, Spider Man entry and Out of Africa entry'
        Customer customer = dataContainer.customer()
        Long customerId = 1L

        Integer spiderManRentalDays = 5
        Integer outOfAfricaRentalDays = 7

        RentFilmEntryDTO spiderManFilmEntry = dataContainer.spiderManEntry(spiderManRentalDays)
        RentFilmEntryDTO outOfAfricaFilm = dataContainer.outOfAfricaEntry(outOfAfricaRentalDays)

        when: 'I add Spider Man to rental box'
        ResultActions spiderManAddedToBoxResultAction = this.mockMvc
                .perform(post('/api/rentals/box')
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(buildJson(spiderManFilmEntry)))

        BigDecimal expectedRentalPriceOfSpiderMan = BigDecimal.valueOf(90)
        RentFilmEntryDTO spiderManWithPrice = spiderManFilmEntry.toBuilder()
                .price(expectedRentalPriceOfSpiderMan)
                .numberOfDays(spiderManRentalDays)
                .build()

        RentalOrderDraftDTO rentalOrderDraft = RentalOrderDraftDTO.builder()
                .films(Arrays.asList(spiderManWithPrice))
                .totalPrice(expectedRentalPriceOfSpiderMan)
                .build()

        then: 'I get rental details with 90 SEK total price'
        httpSession.getId() >> "1"

        spiderManAddedToBoxResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(rentalOrderDraft)))

        when: 'I add Out of Africa to rental box'
        ResultActions outOfAfricaAddedToBoxResultAction = this.mockMvc
                .perform(post('/api/rentals/box')
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(buildJson(outOfAfricaFilm)))

        BigDecimal expectedRentalPriceOfOutOfAfrica = BigDecimal.valueOf(90)
        RentFilmEntryDTO outOfAfricaWithPrice = outOfAfricaFilm.toBuilder()
                .price(expectedRentalPriceOfOutOfAfrica)
                .numberOfDays(outOfAfricaRentalDays)
                .build()

        BigDecimal totalPriceOfRental = BigDecimal.valueOf(180)
        RentalOrderDraftDTO finalRentalOrderDraft = RentalOrderDraftDTO.builder()
                .films(Arrays.asList(spiderManWithPrice, outOfAfricaWithPrice))
                .totalPrice(totalPriceOfRental)
                .build()

        then: 'I get rental details with 180 SEK total price'
        httpSession.getId() >> "1"

        outOfAfricaAddedToBoxResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(finalRentalOrderDraft)))

        when: 'I complete rent'
        httpSession.getId() >> "1"
        ResultActions completeRentResultAction = this.mockMvc
                .perform(post('/api/rentals?customerId={customerId}', String.valueOf(customerId))
                .session(httpSession))

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
                .rentDate(today())
                .build()

        RentalDTO outOfAfricaRental = RentalDTO.builder()
                .id(secondRentalId)
                .customerId(customerId)
                .filmType(FilmType.OLD.name())
                .status(RentalStatus.STARTED.name())
                .surcharge(BigDecimal.ZERO)
                .price(expectedRentalPriceOfOutOfAfrica)
                .expectedReturnDate(todayPlus(outOfAfricaRentalDays))
                .rentDate(today())
                .build()

        RentalOrderDTO expectedRentalOrder = RentalOrderDTO.builder()
                .id(rentalOrderId)
                .totalPrice(expectedTotalPrice)
                .totalSurcharge(BigDecimal.ZERO)
                .rentals(Arrays.asList(spiderManRental, outOfAfricaRental))
                .build()

        then: 'I have to pay 180 SEK for order'
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
                .perform(post('/api/rentals/returns')
                .param('rentalId', String.valueOf(spiderManRentalId)))

        BigDecimal expectedSurcharge = BigDecimal.valueOf(120)

        RentalDTO spiderManRentalAfterReturn = spiderManRental.toBuilder()
                .surcharge(expectedSurcharge)
                .status(RentalStatus.END.name())
                .actualReturnDate(todayPlus(9))
                .build()

        then: 'Rental of Spider Man has been ended and surcharge increased to 120 SEK'
        timeProvider.today() >> todayPlus(9)
        returnSpiderMan
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(spiderManRentalAfterReturn)))

        when: 'I return Out of Africa with 2 days of delay'
        Long outOfAfricaRentalId = 2L
        ResultActions returnOutOfAfrica = this.mockMvc
                .perform(post('/api/rentals/returns')
                .param('rentalId', String.valueOf(outOfAfricaRentalId)))

        BigDecimal outOfAfricaSurcharge = BigDecimal.valueOf(60)

        RentalDTO outOfAfricaAfterReturn = outOfAfricaRental.toBuilder()
                .surcharge(outOfAfricaSurcharge)
                .status(RentalStatus.END.name())
                .actualReturnDate(todayPlus(9))
                .build()

        then: 'Rental of Out of Africa has been ended and surcharge increased to 60 SEK'
        timeProvider.today() >> todayPlus(9)
        returnOutOfAfrica
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(outOfAfricaAfterReturn)))

        when: 'I get details about rental order'
        ResultActions getRentalOrderDetailsResultAction = this.mockMvc
                .perform(get('/api/rentals/{rentalOrderId}', rentalOrderId))

        BigDecimal finalSurcharge = BigDecimal.valueOf(180)
        RentalOrderDTO expectedFinalRentalOrder = expectedRentalOrder.toBuilder()
                .rentals(Arrays.asList(outOfAfricaAfterReturn, spiderManRentalAfterReturn))
                .totalSurcharge(finalSurcharge)
                .build()

        then: 'Final surcharge is 180 SEK'
        getRentalOrderDetailsResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedFinalRentalOrder)))
    }


    def 'should rent regular film and return before end of first days relief'() {
        given: 'Customer and Spider Man entry '
        Customer customer = dataContainer.customer()
        Long customerId = 1L
        Integer numberOfRentalDays = 1
        RentFilmEntryDTO spiderManFilm = dataContainer.spiderManEntry(numberOfRentalDays)

        BigDecimal spiderManPrice = BigDecimal.valueOf(30)

        RentFilmEntryDTO spiderManEntryWithPrice = spiderManFilm.toBuilder()
                .price(spiderManPrice)
                .numberOfDays(numberOfRentalDays)
                .build()

        when: 'I add Spider Man to rental box'
        ResultActions resultAction = this.mockMvc
                .perform(post('/api/rentals/box')
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(buildJson(spiderManFilm)))

        RentalOrderDraftDTO expectedRentalOrderDraft = RentalOrderDraftDTO.builder()
                .films(Arrays.asList(spiderManEntryWithPrice))
                .totalPrice(spiderManPrice)
                .build()

        then: 'I get rental details with 40 SEK total price'
        httpSession.getId() >> "1"

        resultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedRentalOrderDraft)))

        when: 'I complete rent'
        httpSession.getId() >> "1"

        ResultActions completeRentResultAction = this.mockMvc
                .perform(post('/api/rentals?customerId={customerId}', String.valueOf(customerId))
                .session(httpSession))

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
                .rentDate(today())
                .build()

        RentalOrderDTO expectedRentalOrder = RentalOrderDTO.builder()
                .id(rentalOrderId)
                .totalPrice(expectedTotalPrice)
                .totalSurcharge(BigDecimal.ZERO)
                .rentals(Arrays.asList(spiderManRental))
                .build()

        then: 'I have to pay 30 SEK for order'
        completeRentResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedRentalOrder)))

        and: 'I have added 1 bonus point'
        customer.increaseBonusPoints(1)

        Customer actualCustomer = customerRepository.findById(customerId).get()
        actualCustomer.bonusPoints == customer.bonusPoints

        when: 'I return Spider Man day before end of first 3 days of relief'
        Long spiderManRentalId = 1L
        ResultActions returnSpiderManResultAction = this.mockMvc
                .perform(post('/api/rentals/returns')
                .param('rentalId', String.valueOf(spiderManRentalId)))

        RentalDTO spiderManRentalAfterReturn = spiderManRental.toBuilder()
                .surcharge(BigDecimal.valueOf(0))
                .status(RentalStatus.END.name())
                .actualReturnDate(todayPlus(1))
                .build()

        then: 'Rental of Spider Man has been ended without surcharge'
        timeProvider.today() >> todayPlus(1)

        returnSpiderManResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(spiderManRentalAfterReturn)))

        when: 'I get details about rental order'
        ResultActions getRentalOrderDetailsResultAction = this.mockMvc
                .perform(get('/api/rentals/{rentalOrderId}', rentalOrderId))

        BigDecimal finalSurcharge = BigDecimal.valueOf(0)
        RentalOrderDTO expectedFinalRentalOrder = expectedRentalOrder.toBuilder()
                .rentals(Arrays.asList(spiderManRentalAfterReturn))
                .totalSurcharge(finalSurcharge)
                .build()

        then: 'Final surcharge is 0 SEK'
        getRentalOrderDetailsResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedFinalRentalOrder)))
    }

    LocalDate todayPlus(Integer days) {
        return LocalDate.now().plusDays(days)
    }

    LocalDate todayMinus(Integer days) {
        return LocalDate.now().minusDays(days)
    }

    LocalDate today() {
        return LocalDate.now()
    }
}
