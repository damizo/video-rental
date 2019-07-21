package com.casumo.recruitment.videorental.customer;

import com.casumo.recruitment.videorental.infrastructure.exception.Error;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;

public interface CustomerSwaggerDocumentation {

    @ApiOperation(value = "Get details about Customer", response = CustomerDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched details", response = CustomerDTO.class),
            @ApiResponse(code = 417, message = "Business error occured", response = Error.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    CustomerDTO getCustomer(@PathVariable Long customerId);

}
