package com.casumo.recruitment.videorental.infrastructure.utils;

public class SwaggerMessages {

    public static final String BUSSINESS_ERROR = "Business error occured";
    public static final String RESOURCE_NOT_FOUND = "The resource you were trying to reach is not found";

    public static class Film {
        public static final String GET_ALL_FILMS = "Get all films from inventory";
        public static final String GET_ALL_FILMS_SUCCESS = "Successfully fetched inventory";

        public static final String GET_FILM_DETAILS = "Get films details";
        public static final String GET_FILM_DETAILS_SUCCESS = "Successfully returned details about film";

        public static final String ADD_FILM = "Add film to inventory";
        public static final String ADD_FILM_SUCCESS = "Successfully created film";
    }

    public static class Rental {
        public static final String ADD_FILM_TO_RENTAL_BOX = "Add film to rental box";
        public static final String ADD_FILM_TO_RENTAL_BOX_SUCCESS = "Successfully added film to box";

        public static final String REMOVE_FILM_FROM_RENTAL_BOX = "Remove film from rental box";
        public static final String REMOVE_FILM_FROM_RENTAL_BOX_SUCCESS = "Successfully removed film from box";

        public static final String GET_DETAILS_ABOUT_RENTAL_BOX = "Get getails about current rental box";
        public static final String GET_DETAILS_ABOUT_RENTAL_BOX_SUCCESS = "Successfully fetched details about current rental box";

        public static final String COMPLETE_RENTAL_ORDER = "Complete rental order";
        public static final String COMPLETE_RENTAL_ORDER_SUCCESS = "Successfully completed order";

        public static final String GET_DETAILS_ABOUT_RENTAL_ORDER = "Get details about rental order";
        public static final String GET_DETAILS_ABOUT_RENTAL_ORDER_SUCCESS = "Successfully fetched details about rental order";

        public static final String RETURN_FILM = "Return film to inventory";
        public static final String RETURN_FILM_SUCCESS = "Successfully returned film to inventory";

    }

    public static class Customer {

        public static final String GET_DETAILS = "Get details about customer";
        public static final String GET_DETAILS_SUCCESS = "Successfully fetched details about customer";

        public static final String CREATE = "Create new customer";
        public static final String CREATE_SUCCESS = "Successfully created new customer";
    }
}
