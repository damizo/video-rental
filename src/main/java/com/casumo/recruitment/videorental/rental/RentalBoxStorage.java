package com.casumo.recruitment.videorental.rental;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@SessionScope
@Getter
public class RentalBoxStorage {

    private Map<String, RentOrderDraftDTO> boxes = new ConcurrentHashMap<>();

    public RentOrderDraftDTO find(String sessionId) {
        RentOrderDraftDTO rentalOrder = boxes.get(sessionId);
        return Optional.ofNullable(rentalOrder)
                .orElseThrow(() -> new RentalOrderNotFoundException(Collections.singletonMap("sessionId", sessionId)));
    }

    public boolean exists(String sessionId) {
        return Optional.ofNullable(boxes.get(sessionId)).isPresent();
    }

    public RentOrderDraftDTO add(String sessionId, RentFilmEntryDTO entry) {
        RentOrderDraftDTO rentalOrder = find(sessionId);

        if (rentalOrder.getFilms().contains(entry)) {
            throw new FilmAlreadyExistsInBoxException(Collections.singletonMap("id", String.valueOf(entry.getFilmId())));
        }

        rentalOrder.getFilms().add(entry);

        BigDecimal currentFinalPrice = rentalOrder.getTotalPrice().add(entry.getPrice());
        rentalOrder.setTotalPrice(currentFinalPrice);

        return boxes.put(sessionId, rentalOrder);
    }

    public void initialize(String sessionId) {
        boxes.put(sessionId, new RentOrderDraftDTO());
    }

    public void clear(String sessionId) {
        boxes.remove(sessionId);
    }

    public void remove(String sessionId, Long filmId) {
        RentOrderDraftDTO rentalOrderDraft = find(sessionId);
        RentFilmEntryDTO entryToRemove = rentalOrderDraft.getFilms().stream().filter(entry -> entry.getFilmId().equals(filmId))
                .findAny()
                .orElseThrow(() -> new NoSuchFilmInBoxException(Collections.singletonMap("id", String.valueOf(filmId))));

        rentalOrderDraft.getFilms().remove(entryToRemove);
        boxes.put(sessionId, rentalOrderDraft);
    }
}
