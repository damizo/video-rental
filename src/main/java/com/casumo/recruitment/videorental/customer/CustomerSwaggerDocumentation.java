package com.casumo.recruitment.videorental.customer;

import com.casumo.recruitment.videorental.infrastructure.exception.ErrorDTO;
import com.casumo.recruitment.videorental.infrastructure.utils.SwaggerMessages;
import com.casumo.recruitment.videorental.shared.dto.PersonalDataDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CustomerSwaggerDocumentation {

    @ApiOperation(value = SwaggerMessages.Customer.GET_DETAILS, response = CustomerDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerMessages.Customer.GET_DETAILS_SUCCESS, response = CustomerDTO.class),
            @ApiResponse(code = 417, message = SwaggerMessages.BUSSINESS_ERROR, response = ErrorDTO.class),
            @ApiResponse(code = 404, message = SwaggerMessages.RESOURCE_NOT_FOUND)
    })
    CustomerDTO getCustomer(@PathVariable Long customerId);

    @ApiOperation(value = SwaggerMessages.Customer.CREATE, response = CustomerDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerMessages.Customer.CREATE_SUCCESS, response = CustomerDTO.class),
            @ApiResponse(code = 417, message = SwaggerMessages.BUSSINESS_ERROR, response = ErrorDTO.class),
            @ApiResponse(code = 404, message = SwaggerMessages.RESOURCE_NOT_FOUND)
    })
    CustomerDTO create(@RequestBody PersonalDataDTO personalData);
}
