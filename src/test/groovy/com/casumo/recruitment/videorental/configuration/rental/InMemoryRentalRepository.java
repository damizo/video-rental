package com.casumo.recruitment.videorental.configuration.rental;

import com.casumo.recruitment.videorental.rental.Rental;
import com.casumo.recruitment.videorental.rental.RentalRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryRentalRepository implements RentalRepository {
    private Map<Long, Rental> rentals = new ConcurrentHashMap<>();

    @Override
    public Optional<Rental> findById(Long filmId) {
        return Optional.ofNullable(rentals.get(filmId));
    }

    @Override
    public <S extends Rental> S save(S s) {
        Long id = resolveId(s);
        this.rentals.put(id, s);
        s.setId(id);
        return s;
    }

    private <S extends Rental> Long resolveId(S s) {
        return !Optional.ofNullable(s.getId()).isPresent() ? this.rentals.keySet().size() + 1 : s.getId();
    }

    @Override
    public Page<Rental> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean existsById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Rental> findAll() {
        return rentals.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public List<Rental> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Rental> findAllById(Iterable<Long> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Rental film) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends Rental> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        this.rentals = new ConcurrentHashMap<>();
    }

    @Override
    public <S extends Rental> List<S> saveAll(Iterable<S> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Rental> S saveAndFlush(S s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteInBatch(Iterable<Rental> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Rental getOne(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Rental> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Rental> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Rental> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Rental> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Rental> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Rental> boolean exists(Example<S> example) {
        return false;
    }
}
