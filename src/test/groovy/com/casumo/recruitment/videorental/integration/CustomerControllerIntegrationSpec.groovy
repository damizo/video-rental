package com.casumo.recruitment.videorental.integration


import com.casumo.recruitment.videorental.configuration.customer.CustomerConfiguration
import com.casumo.recruitment.videorental.configuration.database.DatabaseConfiguration
import com.casumo.recruitment.videorental.customer.CustomerController
import com.casumo.recruitment.videorental.customer.CustomerDTO
import com.casumo.recruitment.videorental.infrastructure.IntegrationSpec
import com.casumo.recruitment.videorental.infrastructure.config.Profiles
import com.casumo.recruitment.videorental.shared.CurrencyType
import com.casumo.recruitment.videorental.shared.dto.PersonalDataDTO
import com.casumo.recruitment.videorental.shared.time.TimeProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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
        CustomerConfiguration.class,
        TimeProvider.class,
        DatabaseConfiguration.class
])
@SpringBootTest
class CustomerControllerIntegrationSpec extends IntegrationSpec {

    @Autowired
    private CustomerController customerController

    def setup() {

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .setControllerAdvice(restControllerAdvice)
                .alwaysDo(MockMvcResultHandlers.print())
                .build()
        dataContainer.initializeCustomers()
    }


    def cleanup() {
        dataContainer.cleanUp()
    }

    def 'should get details about customer'() {
        when: 'I ask about customer details'
        CustomerDTO customer = dataContainer.customerDTO()
        Long customerId = customer.id

        ResultActions getCustomerDetailsResultAction = this.mockMvc
                .perform(get('/api/customers/{customerId}', customerId)
                .contentType(MediaType.APPLICATION_JSON))

        then: 'I get customer details'
        getCustomerDetailsResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))
    }

    def 'should create new customer'() {
        when: 'I create new customer'
        PersonalDataDTO personalData = PersonalDataDTO.builder()
                .email("john@google.com")
                .firstName("John")
                .lastName("Smith")
                .build()

        ResultActions customerResponse = this.mockMvc
                .perform(post('/api/customers')
                .contentType(MediaType.APPLICATION_JSON)
                .content(buildJson(personalData)))

        then: 'New customer has been created'
        Long customerId = 2L
        CustomerDTO customer = CustomerDTO.builder()
                .id(customerId)
                .bonusPoints(0)
                .personalData(personalData)
                .currency(CurrencyType.SEK.name())
                .build()

        customerResponse
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))

        and: 'Customer is stored in database'
        ResultActions getCustomerDetailsResultAction = this.mockMvc
                .perform(get('/api/customers/{customerId}', customerId)
                .contentType(MediaType.APPLICATION_JSON))

        getCustomerDetailsResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))

    }

    def 'should not create same customer twice'() {
        when: 'I create new customer'
        PersonalDataDTO personalData = PersonalDataDTO.builder()
                .email("john@google.com")
                .firstName("John")
                .lastName("Smith")
                .build()

        ResultActions createCustomerResultAction = this.mockMvc
                .perform(post('/api/customers')
                .contentType(MediaType.APPLICATION_JSON)
                .content(buildJson(personalData)))

        then: 'New customer has been created'
        Long customerId = 2L
        CustomerDTO customer = CustomerDTO.builder()
                .id(customerId)
                .bonusPoints(0)
                .personalData(personalData)
                .currency(CurrencyType.SEK.name())
                .build()

        createCustomerResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))

        and: 'Customer is stored in database'
        ResultActions getCustomerDetailsResultAction = this.mockMvc
                .perform(get('/api/customers/{customerId}', customerId)
                .contentType(MediaType.APPLICATION_JSON))

        getCustomerDetailsResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))

        when: 'I create customer with same data twice'
        ResultActions createCustomerFailureResultAction = this.mockMvc
                .perform(post('/api/customers')
                .contentType(MediaType.APPLICATION_JSON)
                .content(buildJson(personalData)))

        then:
        createCustomerFailureResultAction
                .andExpect(status()
                .is4xxClientError())
    }
}
