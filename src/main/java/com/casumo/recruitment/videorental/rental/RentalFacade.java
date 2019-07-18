package com.casumo.recruitment.videorental.rental;

import com.casumo.recruitment.videorental.sharedkernel.time.TimeProvider;
import com.casumo.recruitment.videorental.calculation.CalculationService;
import com.casumo.recruitment.videorental.customer.Customer;
import com.casumo.recruitment.videorental.customer.CustomerRepository;
import com.casumo.recruitment.videorental.film.FilmRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Transactional
public class RentalFacade {

    private CustomerRepository customerRepository;
    private FilmRepository filmRepository;
    private RentalRepository rentalRepository;
    private TimeProvider timeProvider;

    private CalculationService calculationService;

    public ReturnConfirmation returnFilm(List<Long> rentalIds) {
        List<Rental> rentals = rentalIds
                .stream()
                .map(rentalId -> rentalRepository.findById(rentalId)
                        .orElseThrow(RentalNotFoundException::new))
                .collect(Collectors.toList());

        rentals.forEach(Rental::returnFilm);

        BigDecimal surchargePrice = rentals.stream()
                .map(rental -> calculationService.calculateSurchargePrice(rental.getFilmType(), rental.getExpectedReturnDate(), rental.getActualReturnDate()))
                .reduce(BigDecimal::add)
                .orElseThrow(IllegalArgumentException::new);

        return ReturnConfirmation.builder()
                .rentals(rentals)
                .surcharge(surchargePrice)
                .build();
    }

    public RentConfirmation rent(Long customerId, RentOrder rentFilmOrder) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(IllegalArgumentException::new);

        List<Rental> rentals = rentFilmOrder.getFilms()
                .stream()
                .map(rentFilmEntry -> filmRepository.findById(rentFilmEntry.getFilmId())
                        .map(film -> rentalRepository.save(Rental.builder()
                                .filmType(film.getType())
                                .customer(customer)
                                .rentDate(timeProvider.today())
                                .expectedReturnDate(rentFilmEntry.getExpectedReturnDate())
                                .build())).orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList());

        BigDecimal price = rentals.stream()
                .map(rental -> calculationService.calculateRentPrice(rental.getExpectedReturnDate(), rental.getFilmType()))
                .reduce(BigDecimal::add)
                .orElseThrow(IllegalArgumentException::new);

        customer.increaseBonusPoints();
        return new RentConfirmation(rentals.stream().map(Rental::getId).collect(Collectors.toList()), price);
    }

}
