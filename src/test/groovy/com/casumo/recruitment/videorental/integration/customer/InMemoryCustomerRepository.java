package com.casumo.recruitment.videorental.integration.customer;

import com.casumo.recruitment.videorental.customer.Customer;
import com.casumo.recruitment.videorental.customer.CustomerRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryCustomerRepository implements CustomerRepository {
    private Map<Long, Customer> films = new ConcurrentHashMap<>();

    @Override
    public Optional<Customer> findById(Long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public <S extends Customer> S save(S s) {
        this.films.put(s.getId(), s);
        return s;
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean existsById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Customer> findAll() {
        return films.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public List<Customer> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Customer> findAllById(Iterable<Long> iterable) {
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
    public void delete(Customer film) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends Customer> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Customer> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Customer> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Customer> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Customer getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Customer> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Customer> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Customer> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Customer> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Customer> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Customer> boolean exists(Example<S> example) {
        return false;
    }
}
