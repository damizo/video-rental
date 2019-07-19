package com.casumo.recruitment.videorental.customer;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@AllArgsConstructor
public class CustomerController implements CustomerSwaggerDocumentation {

    private final CustomerFacade customerFacade;

    @GetMapping(value = "/{customerId}")
    public Customer getCustomer(@PathVariable Long customerId) {
        return customerFacade.getCustomer(customerId);
    }

    @PostMapping
    public Customer create(@RequestBody PersonalData personalData) {
        return customerFacade.createClient(personalData);
    }
}
