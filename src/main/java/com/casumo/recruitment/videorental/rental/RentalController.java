package com.casumo.recruitment.videorental.rental;

import com.casumo.recruitment.videorental.film.FilmFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/rental")
@RequiredArgsConstructor
public class RentalController implements RentalSwaggerDocumentation {

    private final RentalFacade rentalFacade;
    private final FilmFacade filmFacade;
    private final RentalBoxStorage rentalBoxStorage;

    @PostMapping("/box")
    public RentOrderDraftDTO addToRentalBox(HttpSession httpSession, @RequestBody RentFilmEntryDTO rentFilmEntry) {
        String sessionId = httpSession.getId();

        if (!rentalBoxStorage.exists(sessionId)) {
            rentalBoxStorage.initialize(sessionId);
        }
        BigDecimal potentialPrice = filmFacade.checkPrice(rentFilmEntry.getFilmId(), rentFilmEntry.getNumberOfDays());
        rentFilmEntry.setPrice(potentialPrice);

        rentalBoxStorage.add(sessionId, rentFilmEntry);
        return rentalBoxStorage.find(sessionId);
    }

    @DeleteMapping("/box")
    public void removeFromRentalBox(HttpSession httpSession, @RequestParam Long filmId) {
        rentalBoxStorage.remove(httpSession.getId(), filmId);
    }

    @GetMapping("/box")
    public RentOrderDraftDTO getCurrentBoxDetails(HttpSession httpSession) {
        return rentalBoxStorage.find(httpSession.getId());
    }

    @PostMapping("/rent")
    public RentOrderDTO rent(@RequestParam Long customerId, HttpSession httpSession) {
        String sessionId = httpSession.getId();
        RentOrderDraftDTO rentalOrderDraft = rentalBoxStorage.find(sessionId);
        RentOrderDTO completedOrder = rentalFacade.completeOrder(customerId, rentalOrderDraft);
        rentalBoxStorage.clear(sessionId);
        return completedOrder;

    }

    @GetMapping("/rent/{rentalOrderId}")
    public RentOrderDTO getDetails(@PathVariable Long rentalOrderId) {
        return rentalFacade.find(rentalOrderId);
    }

    @PostMapping("/return")
    public RentalDTO returnFilm(@RequestParam Long rentalId) {
        return rentalFacade.returnFilm(rentalId);
    }


}
