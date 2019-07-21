package com.casumo.recruitment.videorental.configuration.rental;

import com.casumo.recruitment.videorental.rental.RentalOrder;
import com.casumo.recruitment.videorental.rental.RentalOrderRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryRentalOrderRepository implements RentalOrderRepository {
    private Map<Long, RentalOrder> rentalOrders = new ConcurrentHashMap<>();

    @Override
    public Optional<RentalOrder> findById(Long filmId) {
        return Optional.ofNullable(rentalOrders.get(filmId));
    }

    @Override
    public <S extends RentalOrder> S save(S s) {
        Long id = resolveId(s);
        this.rentalOrders.put(id, s);
        s.setId(id);
        return s;
    }

    private <S extends RentalOrder> Long resolveId(S s) {
        return !Optional.ofNullable(s.getId()).isPresent() ? this.rentalOrders.keySet().size() + 1 : s.getId();
    }

    @Override
    public Page<RentalOrder> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean existsById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<RentalOrder> findAll() {
        return rentalOrders.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public List<RentalOrder> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<RentalOrder> findAllById(Iterable<Long> iterable) {
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
    public void delete(RentalOrder film) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends RentalOrder> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        this.rentalOrders = new ConcurrentHashMap<>();
    }

    @Override
    public <S extends RentalOrder> List<S> saveAll(Iterable<S> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends RentalOrder> S saveAndFlush(S s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteInBatch(Iterable<RentalOrder> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RentalOrder getOne(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends RentalOrder> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends RentalOrder> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends RentalOrder> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends RentalOrder> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends RentalOrder> long count(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends RentalOrder> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException();
    }
}
