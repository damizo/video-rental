package com.casumo.recruitment.videorental.configuration.customer;

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
    private Map<Long, Customer> customers = new ConcurrentHashMap<>();

    @Override
    public Optional<Customer> findById(Long filmId) {
        return Optional.ofNullable(customers.get(filmId));
    }

    @Override
    public Optional<Customer> findByPersonalDataEmail(String email) {
        return customers.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(customer -> customer.getEmail().equals(email))
                .findAny();
    }

    @Override
    public <S extends Customer> S save(S s) {
        Long id = resolveId(s);
        s.setId(id);
        this.customers.put(s.getId(), s);
        return s;
    }


    private <S extends Customer> Long resolveId(S s) {
        int size = this.customers.keySet().size();
        return !Optional.ofNullable(s.getId()).isPresent() ? size + 1 : s.getId();
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
        return customers.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
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
        this.customers = new ConcurrentHashMap<>();
    }

    @Override
    public <S extends Customer> List<S> saveAll(Iterable<S> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Customer> S saveAndFlush(S s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteInBatch(Iterable<Customer> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Customer getOne(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Customer> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Customer> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Customer> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Customer> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
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
