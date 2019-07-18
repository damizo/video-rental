package com.casumo.recruitment.videorental.integration.customer

import com.casumo.recruitment.videorental.DataContainer
import com.casumo.recruitment.videorental.IntegrationSpec
import com.casumo.recruitment.videorental.customer.Customer
import com.casumo.recruitment.videorental.customer.CustomerController
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

@ContextConfiguration(classes = CustomerConfiguration.class)
@EnableSpringDataWebSupport
class CustomerControllerIntegrationSpec extends IntegrationSpec {

    @Autowired
    private CustomerController customerController

    @Autowired
    protected DataContainer dataContainer

    protected MockMvc mockMvc

    def setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .setControllerAdvice(restControllerAdvice)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .alwaysDo(MockMvcResultHandlers.print())
                .build()

        dataContainer.initializeCustomers()
    }

    def 'should get details about customer'() {
        when: 'I ask about customer  details'
        Customer customer = dataContainer.customer()
        Long customerId = customer.id

        ResultActions filmsResponse = this.mockMvc.perform(get('/api/customer/{customerId}', customerId)
                .contentType(MediaType.APPLICATION_JSON_UTF8))

        then: 'I get client customer'
        filmsResponse.andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))
    }

}
