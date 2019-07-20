package com.casumo.recruitment.videorental.configuration.customer;

import com.casumo.recruitment.videorental.customer.CustomerController;
import com.casumo.recruitment.videorental.customer.CustomerFacade;
import com.casumo.recruitment.videorental.customer.CustomerFactory;
import com.casumo.recruitment.videorental.customer.CustomerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerConfiguration {

    @Bean
    public CustomerController customerController(CustomerFacade customerFinder) {
        return new CustomerController(customerFinder);
    }

    @Bean
    public CustomerFacade customerFinder(CustomerRepository customerRepository, CustomerFactory customerFactory) {
        return new CustomerFacade(customerRepository, customerFactory);
    }

    @Bean
    public CustomerRepository customerRepository() {
        return new InMemoryCustomerRepository();
    }

    @Bean
    public CustomerFactory customerFactory() {
        return new CustomerFactory();
    }
}
