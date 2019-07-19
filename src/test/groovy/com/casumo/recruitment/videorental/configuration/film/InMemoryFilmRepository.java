package com.casumo.recruitment.videorental.configuration.film;

import com.casumo.recruitment.videorental.film.Film;
import com.casumo.recruitment.videorental.film.FilmRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryFilmRepository implements FilmRepository {

    private Map<Long, Film> films = new ConcurrentHashMap<>();

    @Override
    public Optional<Film> findById(Long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public <S extends Film> S save(S s) {
        this.films.put(s.getId(), s);
        return s;
    }

    @Override
    public Page<Film> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean existsById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Film> findAll() {
        return films.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public List<Film> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Film> findAllById(Iterable<Long> iterable) {
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
    public void delete(Film film) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends Film> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        this.films = new ConcurrentHashMap<>();
    }

    @Override
    public <S extends Film> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Film> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Film> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Film getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Film> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Film> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Film> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Film> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Film> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Film> boolean exists(Example<S> example) {
        return false;
    }
}
