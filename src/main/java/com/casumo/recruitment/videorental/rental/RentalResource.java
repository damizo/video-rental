package com.casumo.recruitment.videorental.rental;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rental")
@AllArgsConstructor
public class RentalResource {

    private final RentalFacade rentalFacade;

    @PostMapping
    public RentConfirmation rent(@RequestParam Long customerId, RentOrder rentFilmOrder){
        return rentalFacade.rent(customerId, rentFilmOrder);
    }

    @PutMapping
    public ReturnConfirmation returnFilm(@RequestParam List<Long> rentalIds){
        return rentalFacade.returnFilm(rentalIds);
    }

}
