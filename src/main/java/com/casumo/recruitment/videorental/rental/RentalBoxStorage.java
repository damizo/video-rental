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

    private Map<String, RentalOrderDraftDTO> boxes = new ConcurrentHashMap<>();

    public RentalOrderDraftDTO find(String sessionId) {
        RentalOrderDraftDTO rentalOrder = boxes.get(sessionId);
        return Optional.ofNullable(rentalOrder)
                .orElseThrow(() -> new RentalOrderNotFoundException(Collections.singletonMap("sessionId", sessionId)));
    }

    public boolean exists(String sessionId) {
        return Optional.ofNullable(boxes.get(sessionId)).isPresent();
    }

    public RentalOrderDraftDTO add(String sessionId, RentFilmEntryDTO entry) {
        RentalOrderDraftDTO rentalOrderDraft = find(sessionId);

        if (rentalOrderDraft.getFilms().contains(entry)) {
            throw new FilmAlreadyExistsInBoxException(Collections.singletonMap("id", String.valueOf(entry.getFilmId())));
        }

        rentalOrderDraft.getFilms().add(entry);

        BigDecimal currentFinalPrice = rentalOrderDraft.getTotalPrice().add(entry.getPrice());
        rentalOrderDraft.setTotalPrice(currentFinalPrice);

        return boxes.put(sessionId, rentalOrderDraft);
    }

    public void initialize(String sessionId) {
        boxes.put(sessionId, new RentalOrderDraftDTO());
    }

    public void clear(String sessionId) {
        boxes.remove(sessionId);
    }

    public void remove(String sessionId, Long filmId) {
        RentalOrderDraftDTO rentalOrderDraft = find(sessionId);
        RentFilmEntryDTO entryToRemove = rentalOrderDraft.getFilms().stream().filter(entry -> entry.getFilmId().equals(filmId))
                .findAny()
                .orElseThrow(() -> new NoSuchFilmInBoxException(Collections.singletonMap("id", String.valueOf(filmId))));

        rentalOrderDraft.getFilms().remove(entryToRemove);

        BigDecimal currentFinalPrice = rentalOrderDraft.getTotalPrice().subtract(entryToRemove.getPrice());
        rentalOrderDraft.setTotalPrice(currentFinalPrice);

        boxes.put(sessionId, rentalOrderDraft);
    }

}
