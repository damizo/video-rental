package com.casumo.recruitment.videorental.acceptance

import com.casumo.recruitment.videorental.DataContainer
import com.casumo.recruitment.videorental.IntegrationSpec
import com.casumo.recruitment.videorental.configuration.TimeConfiguration
import com.casumo.recruitment.videorental.configuration.customer.CustomerConfiguration
import com.casumo.recruitment.videorental.configuration.database.DatabaseConfiguration
import com.casumo.recruitment.videorental.configuration.rental.RentalConfiguration
import com.casumo.recruitment.videorental.customer.Customer
import com.casumo.recruitment.videorental.customer.CustomerController
import com.casumo.recruitment.videorental.customer.CustomerRepository
import com.casumo.recruitment.videorental.film.FilmType
import com.casumo.recruitment.videorental.rental.*
import com.casumo.recruitment.videorental.shared.time.TimeProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import java.time.LocalDate
import java.util.stream.Collectors
import java.util.stream.Stream

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
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
    private TimeProvider timeProvider

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
        given: 'Customer and rent order with Matrix 11'
        Customer customer = dataContainer.customer()

        RentOrder rentFilmOrder = RentOrder.builder().films(Arrays.asList(
                dataContainer.matrixEntry(1),
        )).build()

        when: 'I rent Matrix'
        ResultActions resultAction = this.mockMvc
                .perform(post('/api/rental/rent')
                .param('customerId', String.valueOf(customer.id))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(buildJson(rentFilmOrder)))

        then: 'I get bill with 40 SEK'
        Long firstRentalId = 1L
        BigDecimal priceToPay = BigDecimal.valueOf(40)

        RentConfirmation expectedConfirmation = RentConfirmation.builder()
                .totalPrice(priceToPay)
                .rentalIds(Arrays.asList(firstRentalId))
                .build()

        resultAction.andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedConfirmation)))

        and: 'I receive 2 points of bonus'
        customer.increaseBonusPoints(2)

        this.mockMvc
                .perform(get('/api/customer/{customerId}', customer.id))
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))

        when: 'I return Matrix with 2 days of delay'
        Rental rental = new Rental(firstRentalId,
                customer,
                LocalDate.now(),
                todayPlus(1),
                todayPlus(3),
                FilmType.NEW_RELEASE)

        ReturnConfirmation bill = new ReturnConfirmation(Collections.singletonList(rental), BigDecimal.valueOf(80))

        ResultActions returnResultAction = this.mockMvc
                .perform(post('/api/rental/return')
                .param('rentalIds', String.valueOf(rental.id)))

        then: 'I received additional bill for Matrix with 80 SEK for 2 days of delay'
        timeProvider.today() >> todayPlus(3)

        returnResultAction.andExpect(status().isOk())
                .andExpect(content().json(buildJson(bill)))
    }

    def 'should rent regular and old film and return lately'() {
        given: 'Customer and rent order with Spider Man and Out of Africa'
        Customer customer = dataContainer.customer()

        RentOrder rentFilmOrder = RentOrder.builder().films(Arrays.asList(
                dataContainer.spiderManEntry(5),
                dataContainer.outOfAfricaEntry(7),
        )).build()

        when: 'I rent Spider Man and Out of Africa'
        ResultActions resultAction = this.mockMvc
                .perform(post('/api/rental/rent')
                .param('customerId', String.valueOf(customer.id))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(buildJson(rentFilmOrder)))

        then: 'I get bill with 180 SEK'
        Long firstRentalId = 1L
        Long secondRentalId = 2L
        BigDecimal priceToPay = BigDecimal.valueOf(180)


        RentConfirmation expectedConfirmation = RentConfirmation.builder()
                .totalPrice(priceToPay)
                .rentalIds(Arrays.asList(firstRentalId, secondRentalId))
                .build()

        resultAction.andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedConfirmation)))

        and: 'I receive 2 points of bonus'
        customer.increaseBonusPoints(2)

        this.mockMvc
                .perform(get('/api/customer/{customerId}', customer.id))
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))

        when: 'I return films with few days of delay'
        Rental spiderManRental = new Rental(firstRentalId,
                customer,
                LocalDate.now(),
                todayPlus(5),
                todayPlus(9),
                FilmType.REGULAR)

        Rental outOfAfricaRental = new Rental(secondRentalId,
                customer,
                LocalDate.now(),
                todayPlus(7),
                todayPlus(9),
                FilmType.OLD)

        ReturnConfirmation bill = new ReturnConfirmation(Arrays.asList(spiderManRental, outOfAfricaRental),
                BigDecimal.valueOf(180))

        ResultActions returnResultAction = this.mockMvc
                .perform(post('/api/rental/return')
                .param('rentalIds', Stream.of(spiderManRental.id, outOfAfricaRental.id)
                .map({ id -> String.valueOf(id) })
                .collect(Collectors.joining(','))))

        then: 'I received additional bill with 180 SEK'
        timeProvider.today() >> todayPlus(9)

        returnResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(bill)))

    }


    def 'should rent regular film and return before end of first days relief'() {
        given: 'Customer and rent order with Spider Man and Out of Africa'
        Customer customer = dataContainer.customer()

        RentOrder rentFilmOrder = RentOrder.builder().films(Arrays.asList(
                dataContainer.spiderManBetterOneEntry(1),
        )).build()

        when: 'I rent Spider Man and Out of Africa'
        ResultActions resultAction = this.mockMvc
                .perform(post('/api/rental/rent')
                .param('customerId', String.valueOf(customer.id))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(buildJson(rentFilmOrder)))

        then: 'I get bill with 30 SEK'
        Long firstRentalId = 1L
        BigDecimal priceToPay = BigDecimal.valueOf(30)


        RentConfirmation expectedConfirmation = RentConfirmation.builder()
                .totalPrice(priceToPay)
                .rentalIds(Arrays.asList(firstRentalId))
                .build()

        resultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedConfirmation)))

        and: 'I receive 1 point of bonus'
        customer.increaseBonusPoints(1)

        this.mockMvc.perform(get('/api/customer/{customerId}', customer.id))
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))

        when: 'I return film before first days relief ends'
        Rental spiderManRental = new Rental(firstRentalId,
                customer,
                LocalDate.now(),
                todayPlus(1),
                todayPlus(2),
                FilmType.REGULAR)

        ReturnConfirmation bill = new ReturnConfirmation(Arrays.asList(spiderManRental),
                BigDecimal.valueOf(0))

        ResultActions returnResultAction = this.mockMvc
                .perform(post('/api/rental/return')
                .param('rentalIds', String.valueOf(spiderManRental.id)))

        then: 'I do not pay surcharge'
        timeProvider.today() >> todayPlus(2)

        returnResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(bill)))
    }

    LocalDate todayPlus(Integer days) {
        return LocalDate.now().plusDays(days);
    }


}
