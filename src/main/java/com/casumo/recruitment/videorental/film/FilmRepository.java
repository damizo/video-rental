package com.casumo.recruitment.videorental.film;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    Optional<Film> findById(Long filmId);

    Optional<Film> findByBarCode(String barCode);

    Optional<Film> findByTitle(String title);
}
