package com.casumo.recruitment.videorental.rental;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@SessionScope
@Getter
public class RentalBoxStorage {

    private Map<String, RentalOrderDraft> boxes = new ConcurrentHashMap<>();

    public RentalOrderDraft find(String sessionId) {
        RentalOrderDraft rentalOrder = boxes.get(sessionId);
        return Optional.ofNullable(rentalOrder)
                .orElseThrow(() -> new RentalOrderNotFoundException(Collections.singletonMap("sessionId", sessionId)));
    }

    public boolean exists(String sessionId) {
        return Optional.ofNullable(boxes.get(sessionId)).isPresent();
    }

    public RentalOrderDraft add(String sessionId, RentFilmEntry entry) {
        RentalOrderDraft rentalOrder = find(sessionId);

        if (rentalOrder.getFilms().contains(entry)) {
            throw new FilmAlreadyExistsInBoxException(Collections.singletonMap("id", String.valueOf(entry.getFilmId())));
        }

        rentalOrder.addEntry(entry);
        rentalOrder.totalPrice(entry.getPrice());
        boxes.put(sessionId, rentalOrder);
        return rentalOrder;
    }

    public void initialize(String sessionId) {
        boxes.put(sessionId, new RentalOrderDraft());
    }

    public void clear(String sessionId) {
        boxes.remove(sessionId);
    }

    public void remove(String sessionId, Long filmId) {
        RentalOrderDraft rentalOrderDraft = find(sessionId);
        RentFilmEntry entryToRemove = rentalOrderDraft.getFilms().stream().filter(entry -> entry.getFilmId().equals(filmId))
                .findAny()
                .orElseThrow(() -> new NoSuchFilmInBoxException(Collections.singletonMap("id", String.valueOf(filmId))));

        rentalOrderDraft.getFilms().remove(entryToRemove);
        boxes.put(sessionId, rentalOrderDraft);
    }
}
