package com.svalero.films.service;


import com.svalero.films.domain.Director;
import com.svalero.films.domain.Director;
import com.svalero.films.exception.ResourceNotFoundException;
import com.svalero.films.repository.DirectorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorService {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(DirectorService.class);
    @Autowired
    private DirectorRepository directorRepository;

    public List<Director> getAllDirectors() {

            logger.info("Getting all directors from db");
            List<Director> director = directorRepository.findAll();
            logger.info("{} directors retrieved", director.size());
            return director;
    }

    public Director getDirectorsById(long id) {
        logger.info("Searching director with ID {}", id);
        Optional<Director> director = directorRepository.findById(id);
        if (director.isEmpty()) {
            logger.info("Director with ID {} not found", id);
            throw new ResourceNotFoundException("Director not found");
        }
        return director.get();
    }

    public Director createDirectors(Director director) {
        logger.info("Creating a new director");
        if (director == null) {
            throw new IllegalArgumentException("Director cannot be null");
        }
        if (director.getName() == null || director.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (director.getLastName() == null || director.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last Name cannot be null or empty");
        }
        if (director.getBirthDate() == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        if (director.getNationality() == null) {
            throw new IllegalArgumentException("Nationality cannot be null");
        }
        if (director.getAwarded() == null) {
            throw new IllegalArgumentException("Awarded flag cannot be null");
        }
        return directorRepository.save(director);
    }

    public Director updateDirector(Long id, Director updatedDirector) {
        logger.info("Updating director with ID {}", id);

        // Verificar si el director existe en la base de datos
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director with ID " + id + " not found"));

        // Validaciones de entrada
        if (updatedDirector == null) {
            throw new IllegalArgumentException("Updated director data cannot be null");
        }
        if (updatedDirector.getName() == null || updatedDirector.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (updatedDirector.getLastName() == null || updatedDirector.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (updatedDirector.getBirthDate() == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        if (updatedDirector.getNationality() == null || updatedDirector.getNationality().trim().isEmpty()) {
            throw new IllegalArgumentException("Nationality cannot be null or empty");
        }
        if (updatedDirector.getAwarded() == null) {
            throw new IllegalArgumentException("Awarded flag cannot be null");
        }

        // Actualizar los valores del director
        director.setName(updatedDirector.getName());
        director.setLastName(updatedDirector.getLastName());
        director.setBirthDate(updatedDirector.getBirthDate());
        director.setNationality(updatedDirector.getNationality());
        director.setAwarded(updatedDirector.getAwarded());

        return directorRepository.save(director);
    }



    public Director updatePartialDirector(Long id, Director director) {
        logger.info("Updating director with ID {}", id);
        Optional<Director> oldDirector = directorRepository.findById(id);
        if (oldDirector.isEmpty()) {
            throw new ResourceNotFoundException("Director not found");
        }
        Director updatedDirector = oldDirector.get();
        if (director.getName() != null) {
            updatedDirector.setName(director.getName());
        }
        if (director.getLastName() != null) {
            updatedDirector.setLastName(director.getLastName());
        }
        if (director.getBirthDate() != null) {
            updatedDirector.setBirthDate(director.getBirthDate());
        }
        if (director.getNationality() != null) {
            updatedDirector.setNationality(director.getNationality());
        }
        if (director.getAwarded() != null) {
            updatedDirector.setAwarded(director.getAwarded());
        }
        return directorRepository.save(updatedDirector);
    }

    public void deleteDirector(Long id) {

        logger.info("Deleting director with ID {}", id);
        Director existingDirector = directorRepository.findById(id).orElse(null);

        if (existingDirector == null) {
            throw new ResourceNotFoundException("Director with ID " + id + " not found");
        }
        directorRepository.delete(existingDirector);
    }

}

