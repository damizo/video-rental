package com.casumo.recruitment.videorental.rental;

import com.casumo.recruitment.videorental.customer.Customer;
import com.casumo.recruitment.videorental.infrastructure.Error;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface RentalSwaggerDocumentation {

    @ApiOperation(value = "Rent films", response = Customer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully rent order completed", response = RentConfirmation.class),
            @ApiResponse(code = 409, message = "Business error occured", response = Error.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    RentConfirmation rent(@RequestParam Long customerId, @RequestBody RentOrder rentFilmOrder);

    @ApiOperation(value = "Return film to inventory", response = ReturnConfirmation.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned film to inventory", response = ReturnConfirmation.class),
            @ApiResponse(code = 409, message = "Business error occured", response = Error.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    ReturnConfirmation returnFilms(@RequestParam List<Long> rentalIds);
}
