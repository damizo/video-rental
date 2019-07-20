package com.casumo.recruitment.videorental.customer;

import com.casumo.recruitment.videorental.infrastructure.Error;
import com.casumo.recruitment.videorental.shared.domain.PersonalData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CustomerSwaggerDocumentation {

    @ApiOperation(value = "Get details about Customer", response = Customer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched details", response = Customer.class),
            @ApiResponse(code = 409, message = "Business error occured", response = Error.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    Customer getCustomer(@PathVariable Long customerId);


    @ApiOperation(value = "Create Customer", response = Customer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created customer", response = Customer.class),
            @ApiResponse(code = 409, message = "Business error occured", response = Error.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    Customer create(@RequestBody PersonalData personalData);


}
