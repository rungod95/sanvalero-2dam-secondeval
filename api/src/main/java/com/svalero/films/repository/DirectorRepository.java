package com.svalero.films.repository;

import com.svalero.films.domain.Director;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DirectorRepository  extends CrudRepository<Director, Integer> {
    List<Director> findAll();
    Optional<Director> findById(long id);
    Director save (Director director);
}
