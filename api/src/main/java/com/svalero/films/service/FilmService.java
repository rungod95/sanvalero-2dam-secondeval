package com.svalero.films.service;

import com.svalero.films.domain.Director;
import com.svalero.films.domain.Film;
import com.svalero.films.exception.ResourceNotFoundException;
import com.svalero.films.repository.DirectorRepository;
import com.svalero.films.repository.FilmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(FilmService.class);
    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private  DirectorRepository directorRepository;

    public List<Film> getAllFilms() {
        logger.info("Getting all films from db");
        List<Film> film = filmRepository.findAll();
        logger.info("{} films retrieved", film.size());
        return film;
    }

    public Film getFilmsById(long id) {
        logger.info("Searching film with ID {}", id);
        Optional<Film> film = filmRepository.findById(id);
        if (film.isEmpty()) {
            logger.info("Film with ID {} not found", id);
            throw new ResourceNotFoundException("Film not found");
        }
        return film.get();
    }

    public Film createFilms(Film film) {
        logger.info("Creating a new film");

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
        // Verificar si el director existe en la base de datos
        if (film.getDirector() == null || film.getDirector().getId() == null) {
            throw new IllegalArgumentException("Director ID cannot be null");
        }

        Director director = directorRepository.findById(film.getDirector().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Director with ID " + film.getDirector().getId() + " not found"));

        // Asignar el director encontrado antes de guardar la película
        film.setDirector(director);

        return filmRepository.save(film);
    }

    public Film updateFilm(Long id, Film film) {
        logger.info("Updating film with ID {}", id);
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

        logger.info("Deleting film with ID {}", id);
        Film existingFilm = filmRepository.findById(id).orElse(null);

        if (existingFilm == null) {
            throw new ResourceNotFoundException("Film with ID " + id + " not found");
        }
        filmRepository.delete(existingFilm);
    }

}

