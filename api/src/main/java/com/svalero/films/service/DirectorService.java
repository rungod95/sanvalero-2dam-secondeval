package com.svalero.films.service;


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

    public Director updateDirector(Long id, Director director) {
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

