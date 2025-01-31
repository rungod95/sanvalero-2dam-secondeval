package com.svalero.films.controller;

import com.svalero.films.domain.Film;
import com.svalero.films.exception.InvalidDataException;
import com.svalero.films.exception.ResourceNotFoundException;
import com.svalero.films.service.FilmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private FilmService filmService;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        logger.info("Starting operation to retrieve all films");
        List<Film> films = new ArrayList<>();
        try {
            films = filmService.getAllFilms();
            logger.info("Operation completed: {} films retrieved", films.size());
        } catch (ResourceNotFoundException e) {
            logger.info("No films found");
            return new ResponseEntity<>(films, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(films, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Long id) {
        logger.info("Starting operation to retrieve films by ID");
        Film film = filmService.getFilmsById(id);
        logger.info("Operation completed: film retrieved with ID {}", id);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        try {
            logger.info("Starting operation to create a film");
            Film newFilm = filmService.createFilms(film);
            logger.info("Film created");
            return new ResponseEntity<>(newFilm, HttpStatus.CREATED);
        } catch (InvalidDataException e) {
            logger.info("Error creating the film: Bad Request");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable Long id, @RequestBody Film film) {
        try {
            logger.info("Starting operation to modify a film");
            Film updatedFilm = filmService.updateFilm(id, film);
            logger.info("Operation completed: film with ID {} has been modified", id);
            return new ResponseEntity<>(updatedFilm, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            logger.info("Film not found with id {}", id);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        try {
            logger.info("Starting operation to delete a film");
            filmService.deleteFilm(id);
            logger.info("Operation completed: film deleted with id {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            logger.info("Film not found with id {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
