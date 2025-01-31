package com.svalero.films.repository;

import com.svalero.films.domain.Film;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FilmRepository extends CrudRepository<Film, Integer> {
    List<Film> findAll();
    Optional<Film> findById(long id);
    Film save (Film film);
}
