package com.casumo.recruitment.videorental.customer;

import com.casumo.recruitment.videorental.shared.dto.PersonalDataDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
public class CustomerController implements CustomerSwaggerDocumentation {

    private final CustomerFacade customerFacade;

    @GetMapping(value = "/{customerId}")
    public CustomerDTO getCustomer(@PathVariable Long customerId) {
        return customerFacade.getCustomer(customerId);
    }

    @PostMapping
    public CustomerDTO create(@RequestBody PersonalDataDTO personalData) {
        return customerFacade.createCustomer(personalData);
    }
}
