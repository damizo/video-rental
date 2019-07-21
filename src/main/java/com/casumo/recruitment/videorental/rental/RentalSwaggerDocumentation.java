package com.casumo.recruitment.videorental.rental;

import com.casumo.recruitment.videorental.infrastructure.exception.ErrorDTO;
import com.casumo.recruitment.videorental.infrastructure.utils.SwaggerMessages;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

public interface RentalSwaggerDocumentation {

    @ApiOperation(value = SwaggerMessages.Rental.ADD_FILM_TO_RENTAL_BOX, response = RentalOrderDraftDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerMessages.Rental.ADD_FILM_TO_RENTAL_BOX_SUCCESS, response = RentalOrderDraftDTO.class),
            @ApiResponse(code = 417, message = SwaggerMessages.BUSSINESS_ERROR, response = ErrorDTO.class),
            @ApiResponse(code = 404, message = SwaggerMessages.RESOURCE_NOT_FOUND)
    })
    RentalOrderDraftDTO addToRentalBox(@ApiIgnore HttpSession httpSession, @RequestBody RentFilmEntryDTO rentFilmEntry);

    @ApiOperation(value = SwaggerMessages.Rental.REMOVE_FILM_FROM_RENTAL_BOX)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerMessages.Rental.REMOVE_FILM_FROM_RENTAL_BOX_SUCCESS, response = RentalOrderDraftDTO.class),
            @ApiResponse(code = 417, message = SwaggerMessages.BUSSINESS_ERROR, response = ErrorDTO.class),
            @ApiResponse(code = 404, message = SwaggerMessages.RESOURCE_NOT_FOUND)
    })
    void removeFromRentalBox(@ApiIgnore HttpSession httpSession, @RequestParam Long filmId);

    @ApiOperation(value = SwaggerMessages.Rental.GET_DETAILS_ABOUT_RENTAL_BOX, response = RentalOrderDraftDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerMessages.Rental.GET_DETAILS_ABOUT_RENTAL_BOX_SUCCESS, response = RentalOrderDraftDTO.class),
            @ApiResponse(code = 417, message = SwaggerMessages.BUSSINESS_ERROR, response = ErrorDTO.class),
            @ApiResponse(code = 404, message = SwaggerMessages.RESOURCE_NOT_FOUND)
    })
    RentalOrderDraftDTO getCurrentBoxDetails(@ApiIgnore HttpSession httpSession);

    @ApiOperation(value = SwaggerMessages.Rental.COMPLETE_RENTAL_ORDER, response = RentalOrderDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerMessages.Rental.COMPLETE_RENTAL_ORDER_SUCCESS, response = RentalOrderDTO.class),
            @ApiResponse(code = 417, message = SwaggerMessages.BUSSINESS_ERROR, response = ErrorDTO.class),
            @ApiResponse(code = 404, message = SwaggerMessages.RESOURCE_NOT_FOUND)
    })
    RentalOrderDTO rent(@RequestParam Long customerId, @ApiIgnore HttpSession httpSession);

    @ApiOperation(value = SwaggerMessages.Rental.GET_DETAILS_ABOUT_RENTAL_ORDER, response = RentalOrderDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerMessages.Rental.GET_DETAILS_ABOUT_RENTAL_ORDER_SUCCESS, response = RentalOrderDTO.class),
            @ApiResponse(code = 417, message = SwaggerMessages.BUSSINESS_ERROR, response = ErrorDTO.class),
            @ApiResponse(code = 404, message = SwaggerMessages.RESOURCE_NOT_FOUND)
    })
    RentalOrderDTO getDetails(@PathVariable Long rentalOrderId);

    @ApiOperation(value = SwaggerMessages.Rental.RETURN_FILM, response = RentalDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerMessages.Rental.RETURN_FILM_SUCCESS, response = RentalDTO.class),
            @ApiResponse(code = 417, message = SwaggerMessages.BUSSINESS_ERROR, response = ErrorDTO.class),
            @ApiResponse(code = 404, message = SwaggerMessages.RESOURCE_NOT_FOUND)
    })
    RentalDTO returnFilm(@RequestParam Long rentalId);
}
