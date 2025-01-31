package com.svalero.films.controller;

import com.svalero.films.domain.Film;
import com.svalero.films.exception.InvalidDataException;
import com.svalero.films.exception.ResourceNotFoundException;
import com.svalero.films.service.FilmService;
import org.apache.coyote.Response;
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

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        List<Film> films = new ArrayList<>();
        try {
             films = filmService.getAllFilms();
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(films, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(films, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Long id) {
        Film film = filmService.getFilmsById(id);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        try {
            Film newFilm = filmService.createFilms(film);
            return new ResponseEntity<>(newFilm, HttpStatus.CREATED);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable Long id, @RequestBody Film film) {
        try {
            Film updatedFilm = filmService.updateFilm(id, film);
            return new ResponseEntity<>(updatedFilm, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        try {
            filmService.deleteFilm(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
