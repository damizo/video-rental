package com.casumo.recruitment.videorental.integration.customer;

import com.casumo.recruitment.videorental.customer.CustomerFinder;
import com.casumo.recruitment.videorental.customer.CustomerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerConfiguration {

    @Bean
    public CustomerFinder customerFinder(CustomerRepository customerRepository) {
        return new CustomerFinder(customerRepository);
    }

    @Bean
    public CustomerRepository customerRepository() {
        return new InMemoryCustomerRepository();
    }
}
