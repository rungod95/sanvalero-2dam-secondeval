package com.svalero.films.service;

import com.svalero.films.domain.Film;
import com.svalero.films.exception.ResourceNotFoundException;
import com.svalero.films.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {

    @Autowired
    private FilmRepository filmRepository;

    public List<Film> getAllFilms() {
        try {
            List<Film> film = filmRepository.findAll();
            if (film.isEmpty()) {
                throw new ResourceNotFoundException("Films not found");
            }
            return film;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error while retrieving films");
        }
    }

    public Film getFilmsById(long id) {
        Optional<Film> film = filmRepository.findById(id);
        if (film.isEmpty()) {
            throw new ResourceNotFoundException("Film not found");
        }
        return film.get();
    }

    public Film createFilms(Film film) {
        if (film == null) {
            throw new IllegalArgumentException("Film cannot be null");
        }
        if (film.getTitle() == null || film.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (film.getGenre() == null || film.getGenre().trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be null or empty");
        }
        if (film.getReleaseDate() == null) {
            throw new IllegalArgumentException("Release date cannot be null");
        }
        if (film.getDuration() == null) {
            throw new IllegalArgumentException("Duration cannot be null");
        }
        if (film.getViewed() == null) {
            throw new IllegalArgumentException("Viewed flag cannot be null");
        }
        if (film.getDirector() == null) {
            throw new IllegalArgumentException("Director cannot be null");
        }
        return filmRepository.save(film);
    }

    public Film updateFilm(Long id, Film film) {
        Optional<Film> oldFilm = filmRepository.findById(id);
        if (oldFilm.isEmpty()) {
            throw new ResourceNotFoundException("Film not found");
        }
        Film updatedFilm = oldFilm.get();
        if (film.getTitle() != null) {
            updatedFilm.setTitle(film.getTitle());
        }
        if (film.getGenre() != null) {
            updatedFilm.setGenre(film.getGenre());
        }
        if (film.getReleaseDate() != null) {
            updatedFilm.setReleaseDate(film.getReleaseDate());
        }
        if (film.getDuration() != null) {
            updatedFilm.setDuration(film.getDuration());
        }
        if (film.getViewed() != null) {
            updatedFilm.setViewed(film.getViewed());
        }
        return filmRepository.save(updatedFilm);
    }

    public void deleteFilm(Long id) {

        Film existingFilm = filmRepository.findById(id).orElse(null);

        if (existingFilm == null) {
            throw new IllegalArgumentException("Film with ID " + id + " not found");
        }
        filmRepository.delete(existingFilm);
    }

}

