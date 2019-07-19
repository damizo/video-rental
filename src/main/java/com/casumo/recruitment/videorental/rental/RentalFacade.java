package com.casumo.recruitment.videorental.rental;

import com.casumo.recruitment.videorental.customer.Customer;
import com.casumo.recruitment.videorental.customer.CustomerRepository;
import com.casumo.recruitment.videorental.film.FilmRepository;
import com.casumo.recruitment.videorental.shared.time.TimeProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class RentalFacade {

    private CustomerRepository customerRepository;
    private FilmRepository filmRepository;
    private RentalRepository rentalRepository;
    private TimeProvider timeProvider;

    public ReturnConfirmation returnFilms(List<Long> rentalIds) {
        List<Rental> rentals = findRentals(rentalIds);
        rentals.forEach(rental -> {
            rental.returnFilm(timeProvider.today());
        });

        BigDecimal surchargePrice = calculateSurchargeForEachRental(rentals);

        return ReturnConfirmation.builder()
                .rentals(rentals)
                .surcharge(surchargePrice)
                .build();
    }

    public RentConfirmation rent(Long customerId, RentOrder rentFilmOrder) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(IllegalArgumentException::new);

        List<Rental> rentals = createRentalsForCustomer(rentFilmOrder, customer);
        BigDecimal price = calculatePriceFromRentals(rentals);

        rentals.stream().map(Rental::getFilmType)
                .forEach(filmType -> {
                    customer.increaseBonusPoints(filmType.getBonusPoints());
                });

        return new RentConfirmation(rentals.stream().map(Rental::getId).collect(Collectors.toList()), price);
    }

    private BigDecimal calculatePriceFromRentals(List<Rental> rentals) {
        return rentals.stream()
                .map(Rental::getPrice)
                .reduce(BigDecimal::add)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<Rental> findRentals(List<Long> rentalIds) {
        return rentalIds
                .stream()
                .map(rentalId -> rentalRepository.findById(rentalId)
                        .orElseThrow(RentalNotFoundException::new))
                .collect(Collectors.toList());
    }

    private BigDecimal calculateSurchargeForEachRental(List<Rental> rentals) {
        return rentals.stream()
                .map(Rental::getSurcharge)
                .reduce(BigDecimal::add)
                .orElseThrow(IllegalArgumentException::new);
    }


    private List<Rental> createRentalsForCustomer(RentOrder rentFilmOrder, Customer customer) {
        return rentFilmOrder.getFilms()
                .stream()
                .map(rentFilmEntry -> filmRepository.findById(rentFilmEntry.getFilmId())
                        .map(film -> rentalRepository.save(Rental.builder()
                                .filmType(film.getType())
                                .customer(customer)
                                .rentDate(timeProvider.today())
                                .expectedReturnDate(timeProvider.today().plusDays(rentFilmEntry.getNumberOfDays()))
                                .build()))
                        .orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList());
    }
}
