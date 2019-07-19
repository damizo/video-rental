package com.casumo.recruitment.videorental.rental;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/rental")
@AllArgsConstructor
public class RentalController implements RentalSwaggerDocumentation {

    private final RentalFacade rentalFacade;

    @PostMapping("/rent")
    public RentConfirmation rent(@RequestParam Long customerId, @RequestBody RentOrder rentFilmOrder){
        return rentalFacade.rent(customerId, rentFilmOrder);
    }

    @PostMapping("/return")
    public ReturnConfirmation returnFilms(@RequestParam List<Long> rentalIds){
        return rentalFacade.returnFilms(rentalIds);
    }

}
