package com.casumo.recruitment.videorental.film;

import com.casumo.recruitment.videorental.infrastructure.exception.ErrorDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface FilmSwaggerDocumentation {
    @ApiOperation(value = "Get all films from inventory", response = FilmDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned inventory", response = FilmDTO.class, responseContainer = "List"),
            @ApiResponse(code = 417, message = "Business error occured", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    List<FilmDTO> getFilms();


    @ApiOperation(value = "Get film details", response = FilmDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned details about film", response = FilmDTO.class),
            @ApiResponse(code = 417, message = "Business error occured", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    FilmDTO getFilm(@PathVariable Long filmId);

    @ApiOperation(value = "Create film", response = FilmDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created film", response = FilmDTO.class),
            @ApiResponse(code = 417, message = "Business error occured", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    FilmDTO addFilm(@RequestBody FilmDTO film);

}
