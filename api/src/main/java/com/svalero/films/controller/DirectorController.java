package com.svalero.films.controller;


import com.svalero.films.domain.Director;
import com.svalero.films.domain.Film;
import com.svalero.films.exception.InvalidDataException;
import com.svalero.films.exception.ResourceNotFoundException;
import com.svalero.films.service.DirectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("/directors")
public class DirectorController {

    @Autowired
    private DirectorService directorService;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(DirectorController.class);

    @GetMapping
    public ResponseEntity<List<Director>> getAllDirectors(@RequestParam(value = "genre", defaultValue = "") String genre) {
        logger.info("Starting operation to retrieve all directors");
        logger.info("genre: {}", genre);
        List<Director> director = new ArrayList<>();
        try {
            director = directorService.getAllDirectors(genre);
            logger.info("Operation completed: {} directors retrieved", director.size());
        } catch (ResourceNotFoundException e) {
            logger.info("No directors found");
            return new ResponseEntity<>(director, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(director, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Director> getDirectorById(@PathVariable Long id) {
        logger.info("Starting operation to retrieve directors by ID");
        Director director = directorService.getDirectorsById(id);
        logger.info("Operation completed: director retrieved with ID {}", id);
        return new ResponseEntity<>(director, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createDirector(@RequestBody Director director) {
        try {
            logger.info("Starting operation to create a director");
            Director newDirector = directorService.createDirectors(director);
            logger.info("Director created successfully");
            return new ResponseEntity<>(newDirector, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating the director: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error: " + e.getMessage());
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDirector(@PathVariable Long id, @RequestBody Director director) {
        try {
            logger.info("Starting operation to modify a director with id {}", id);
            Director updatedDirector = directorService.updateDirector(id, director);
            logger.info("Operation completed: director with ID {} has been modified", id);
            return new ResponseEntity<>(updatedDirector, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            logger.error("Error updating director: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            logger.error("Error updating director: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error: " + e.getMessage());
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Director> updatePartialDirector(@PathVariable Long id, @RequestBody Director director) {
        try {
            logger.info("Starting operation to modify a director");
            Director updatedDirector = directorService.updatePartialDirector(id, director);
            logger.info("Operation completed: director with ID {} has been modified", id);
            return new ResponseEntity<>(updatedDirector, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            logger.info("Director not found with id {}", id);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        try {
            logger.info("Starting operation to delete a director");
            directorService.deleteDirector(id);
            logger.info("Operation completed: director deleted with id {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            logger.info("Director not found with id {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}






