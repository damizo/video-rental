package com.casumo.recruitment.videorental.film;

import com.casumo.recruitment.videorental.infrastructure.exception.ErrorDTO;
import com.casumo.recruitment.videorental.infrastructure.utils.SwaggerMessages;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface FilmSwaggerDocumentation {
    @ApiOperation(value = SwaggerMessages.Film.GET_ALL_FILMS, response = FilmDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerMessages.Film.GET_ALL_FILMS_SUCCESS, response = FilmDTO.class, responseContainer = "List"),
            @ApiResponse(code = 417, message = SwaggerMessages.BUSSINESS_ERROR, response = ErrorDTO.class),
            @ApiResponse(code = 404, message = SwaggerMessages.RESOURCE_NOT_FOUND)
    })
    List<FilmDTO> getFilms();


    @ApiOperation(value = SwaggerMessages.Film.GET_FILM_DETAILS, response = FilmDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerMessages.Film.GET_FILM_DETAILS_SUCCESS, response = FilmDTO.class),
            @ApiResponse(code = 417, message = SwaggerMessages.BUSSINESS_ERROR, response = ErrorDTO.class),
            @ApiResponse(code = 404, message = SwaggerMessages.RESOURCE_NOT_FOUND)
    })
    FilmDTO getFilm(@PathVariable Long filmId);

    @ApiOperation(value = SwaggerMessages.Film.ADD_FILM, response = FilmDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerMessages.Film.ADD_FILM_SUCCESS, response = FilmDTO.class),
            @ApiResponse(code = 417, message = SwaggerMessages.BUSSINESS_ERROR, response = ErrorDTO.class),
            @ApiResponse(code = 404, message = SwaggerMessages.RESOURCE_NOT_FOUND)
    })
    FilmDTO addFilm(@RequestBody FilmDTO film);

}
