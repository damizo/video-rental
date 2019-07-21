package com.casumo.recruitment.videorental.customer;

import com.casumo.recruitment.videorental.shared.domain.PersonalData;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@AllArgsConstructor
public class CustomerController implements CustomerSwaggerDocumentation {

    private final CustomerFacade customerFacade;

    @GetMapping(value = "/{customerId}")
    public CustomerDTO getCustomer(@PathVariable Long customerId) {
        return customerFacade.getCustomer(customerId);
    }

    @PostMapping
    public CustomerDTO create(@RequestBody PersonalData personalData) {
        return customerFacade.createCustomer(personalData);
    }
}
