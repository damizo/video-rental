package com.casumo.recruitment.videorental.film;

import com.casumo.recruitment.videorental.customer.Customer;
import com.casumo.recruitment.videorental.infrastructure.Error;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface FilmSwaggerDocumentation {
    @ApiOperation(value = "Get all films from inventory", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned inventory", response = Film.class, responseContainer = "List"),
            @ApiResponse(code = 409, message = "Business error occured", response = Error.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    List<Film> getFilms();


    @ApiOperation(value = "Create Customer", response = Customer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned details about film", response = Film.class),
            @ApiResponse(code = 409, message = "Business error occured", response = Error.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    Film getFilm(@PathVariable Long filmId);
}
