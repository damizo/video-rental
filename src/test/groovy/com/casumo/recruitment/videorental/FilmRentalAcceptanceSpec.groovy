package com.casumo.recruitment.videorental


import com.casumo.recruitment.videorental.film.Film
import com.casumo.recruitment.videorental.rental.RentFilmEntry
import com.casumo.recruitment.videorental.rental.RentOrder
import com.casumo.recruitment.videorental.rental.RentalResource
import com.casumo.recruitment.videorental.rental.ReturnConfirmation
import com.casumo.recruitment.videorental.sharedkernel.time.TimeProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = VideoRentalApplication.class)
@ContextConfiguration(classes = FilmRentalConfiguration.class)
class FilmRentalAcceptanceSpec extends IntegrationSpec {

    @Autowired
    private DataContainer filmContainer

    @Autowired
    private RentalResource rentalResource

    @Autowired
    private TimeProvider timeProvider

    protected MockMvc mockMvc

    def setup() {
        filmContainer.initialize()
        mockMvc = MockMvcBuilders
                .standaloneSetup(rentalResource)
                .alwaysDo(MockMvcResultHandlers.print())
                .build()

    }

    def 'should rent new release and regular film for few days'() {
        when: 'I rent Spider Man and Matrix for 3 days'
        Long customerId = DataContainer.CUSTOMER_ID;
        RentFilmEntry matrixEntry = new RentFilmEntry(DataContainer.MATRIX_ID, 3)
        RentFilmEntry spiderManEntry = new RentFilmEntry(DataContainer.SPIDER_MAN_ID, 3)
        RentOrder rentFilmOrder = RentOrder.of(Arrays.asList(
                matrixEntry,
                spiderManEntry
        ))

        ResultActions resultAction = this.mockMvc.perform(post('/api/rental')
                .param('customerId', String.valueOf(customerId))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(buildJson(rentFilmOrder)))

        then: 'Bill amount is 220'
        BigDecimal priceToPay = BigDecimal.of(220)
        ReturnConfirmation expectedBill = new ReturnConfirmation(customerId, priceToPay, Arrays.asList(matrixEntry, spiderManEntry));

        MvcResult result = resultAction.andExpect(status().isOk())
                .andExpect(content().json(buildJson(expectedBill)))

        and: 'Quantity of Spider Man and Matrix have been decreased in inventory'


        when: 'I return films after 5 days'
        MvcResult returnResult = this.mockMvc.perform(post('/api/rental/{rentalId}', DataContainer.RENTAL_ID))
                .andReturn()

        then: 'I received additional bill with 160 for 2 extra days '
        ReturnConfirmation bill = buildObject(returnResult.getResponse().getContentAsString(), ReturnConfirmation.class)

        bill.getAmount() == BigDecimal.of(160)

        then: 'Films come back to inventory with initial quantity'
        MvcResult matrixResponse = this.mockMvc.perform(get('/api/film/{filmId}', DataContainer.MATRIX_ID))
                .andReturn()

        Film matrix = buildObject(matrixResponse
                .getResponse()
                .getContentAsString(), Film.class)

        MvcResult spiderManResponse = this.mockMvc.perform(get('/api/film/{filmId}', DataContainer.SPIDER_MAN_ID))
                .andReturn()

        Film spiderMan = buildObject(spiderManResponse
                .getResponse()
                .getContentAsString(), Film.class)

        matrix.quantity == 3
        spiderMan.quantity == 3
    }


}
