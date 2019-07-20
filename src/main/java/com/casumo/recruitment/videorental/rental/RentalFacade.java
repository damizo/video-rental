package com.casumo.recruitment.videorental.rental;

import com.casumo.recruitment.videorental.customer.Customer;
import com.casumo.recruitment.videorental.customer.CustomerRepository;
import com.casumo.recruitment.videorental.film.Film;
import com.casumo.recruitment.videorental.film.FilmNotFoundException;
import com.casumo.recruitment.videorental.film.FilmRepository;
import com.casumo.recruitment.videorental.shared.time.TimeProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class RentalFacade {

    private CustomerRepository customerRepository;
    private FilmRepository filmRepository;
    private RentalRepository rentalRepository;
    private RentalOrderRepository orderRepository;
    private RentalFactory rentalFactory;
    private RentalMapper rentalMapper;
    private TimeProvider timeProvider;

    public RentalOrderDTO completeOrder(Long customerId, RentalOrderDraft rentalOrderDraft) {
        List<Rental> rentals = rentalOrderDraft.getFilms()
                .stream()
                .map(rentFilmEntry -> rent(customerId, rentFilmEntry.getFilmId(), rentFilmEntry.getNumberOfDays()))
                .collect(Collectors.toList());

        RentalOrder newRentalOrder = rentalFactory.create(rentals);
        newRentalOrder = orderRepository.save(newRentalOrder);
        return rentalMapper.toDTO(newRentalOrder);
    }

    public RentalDTO returnFilm(Long id) {
        return rentalRepository.findById(id)
                .map(rental -> rental.returnFilm(timeProvider.today()))
                .map(rental -> rentalMapper.toDTO(rental))
                .orElseThrow(() -> new ReturnFilmException(Collections.singletonMap("id", String.valueOf(id))));
    }

    public RentalOrderDTO find(Long rentalOrderId) {
        RentalOrder rentalOrder = orderRepository.findById(rentalOrderId)
                .orElseThrow(() -> new RentalOrderNotFoundException(Collections.singletonMap("id", String.valueOf(rentalOrderId))));
        return rentalMapper.toDTO(rentalOrder);
    }

    private Rental rent(Long customerId, Long filmId, Integer numberOfDays) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(IllegalArgumentException::new);

        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(Collections.singletonMap("id", String.valueOf(filmId))));

        customer.increaseBonusPoints(film.getType().getBonusPoints());

        LocalDate today = timeProvider.today();
        Rental rental = rentalFactory.create(customer, today, film.getType(), today.plusDays(numberOfDays));

        return rentalRepository.save(rental);
    }
}
