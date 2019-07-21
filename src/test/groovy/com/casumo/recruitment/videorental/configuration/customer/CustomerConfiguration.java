package com.casumo.recruitment.videorental.configuration.customer;

import com.casumo.recruitment.videorental.customer.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerConfiguration {

    @Bean
    public CustomerController customerController(CustomerFacade customerFacade) {
        return new CustomerController(customerFacade);
    }

    @Bean
    public CustomerFacade customerFacade(CustomerRepository customerRepository,
                                         CustomerFactory customerFactory, CustomerMapper customerMapper) {
        return new CustomerFacade(customerRepository, customerFactory, customerMapper);
    }

    @Bean
    public CustomerRepository customerRepository() {
        return new InMemoryCustomerRepository();
    }

    @Bean
    public CustomerFactory customerFactory() {
        return new CustomerFactory();
    }

    @Bean
    public CustomerMapper customerMapper() {
        return new CustomerMapper();
    }
}
