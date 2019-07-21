package com.casumo.recruitment.videorental.rental;

import com.casumo.recruitment.videorental.infrastructure.Error;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

public interface RentalSwaggerDocumentation {

    @ApiOperation(value = "Add film to temporary rental box", response = RentOrderDraftDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added film to box", response = RentOrderDraftDTO.class),
            @ApiResponse(code = 409, message = "Business error occured", response = Error.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    RentOrderDraftDTO addToRentalBox(@ApiParam(hidden = true) HttpSession httpSession, @RequestBody RentFilmEntryDTO rentFilmEntry);

    @ApiOperation(value = "Remove film from temporary rental box")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully removed film from rental box", response = RentOrderDraftDTO.class),
            @ApiResponse(code = 409, message = "Business error occured", response = Error.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    void removeFromRentalBox(HttpSession httpSession, @RequestParam Long filmId);

    @ApiOperation(value = "Get details about current rental box", response = RentOrderDraftDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched rental box details", response = RentOrderDraftDTO.class),
            @ApiResponse(code = 409, message = "Business error occured", response = Error.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    RentOrderDraftDTO getCurrentBoxDetails(@ApiParam(hidden = true) HttpSession httpSession);

    @ApiOperation(value = "Rent films from your rental box", response = RentOrderDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully completed rental order", response = RentOrderDTO.class),
            @ApiResponse(code = 409, message = "Business error occured", response = Error.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    RentOrderDTO rent(@RequestParam Long customerId, @ApiParam(hidden = true) HttpSession httpSession);

    @ApiOperation(value = "Get details about rental order", response = RentOrderDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched rental order details", response = RentOrderDTO.class),
            @ApiResponse(code = 409, message = "Business error occured", response = Error.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    RentOrderDTO getDetails(@PathVariable Long rentalOrderId);

    @ApiOperation(value = "Return film to inventory", response = RentalDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned film to inventory", response = RentalDTO.class),
            @ApiResponse(code = 409, message = "Business error occured", response = Error.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    RentalDTO returnFilm(@RequestParam Long rentalId);
}
