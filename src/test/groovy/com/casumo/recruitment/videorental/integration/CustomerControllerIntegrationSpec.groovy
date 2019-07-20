package com.casumo.recruitment.videorental.integration

import com.casumo.recruitment.videorental.infrastructure.DataContainer
import com.casumo.recruitment.videorental.IntegrationSpec
import com.casumo.recruitment.videorental.configuration.customer.CustomerConfiguration
import com.casumo.recruitment.videorental.configuration.database.DatabaseConfiguration
import com.casumo.recruitment.videorental.customer.Customer
import com.casumo.recruitment.videorental.customer.CustomerController
import com.casumo.recruitment.videorental.shared.domain.PersonalData
import com.casumo.recruitment.videorental.shared.time.TimeProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ContextConfiguration(classes = [
        CustomerConfiguration.class,
        TimeProvider.class,
        DatabaseConfiguration.class
])
@SpringBootTest
class CustomerControllerIntegrationSpec extends IntegrationSpec {

    @Autowired
    private CustomerController customerController

    @Autowired
    protected DataContainer dataContainer

    protected MockMvc mockMvc

    def setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .alwaysDo(MockMvcResultHandlers.print())
                .build()

        dataContainer.initializeCustomers()
    }


    def cleanup() {
        dataContainer.cleanUp()
    }

    def 'should get details about customer'() {
        when: 'I ask about customer details'
        Customer customer = dataContainer.customer()
        Long customerId = customer.id

        ResultActions getCustomerDetailsResultAction = this.mockMvc
                .perform(get('/api/customer/{customerId}', customerId)
                .contentType(MediaType.APPLICATION_JSON))

        then: 'I get customer details'
        getCustomerDetailsResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))
    }

    def 'should create new customer'() {
        when: 'I create new customer'
        PersonalData personalData = PersonalData.builder()
                .email("john@google.com")
                .firstName("John")
                .lastName("Smith")
                .build()

        ResultActions customerResponse = this.mockMvc
                .perform(post('/api/customer')
                .contentType(MediaType.APPLICATION_JSON)
                .content(buildJson(personalData)))

        then: 'New customer has been created'
        Long customerId = 2L;
        Customer customer = Customer.builder()
                .id(customerId)
                .personalData(personalData)
                .build()

        customerResponse
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))

        and: 'Is stored in database'
        ResultActions getCustomerDetailsResultAction = this.mockMvc
                .perform(get('/api/customer/{customerId}', customerId)
                .contentType(MediaType.APPLICATION_JSON))

        getCustomerDetailsResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))

    }
}
