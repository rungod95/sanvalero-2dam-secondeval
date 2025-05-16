package com.svalero.films.service;

import com.svalero.films.domain.Film;
import com.svalero.films.repository.FilmRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmServiceTests {

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private FilmService filmService;

    @Test
    void testGetAllFilmsNoGenre() {
        List<Film> mockFilms = List.of(
            new Film(1, "Inception", "Sci-Fi", LocalDate.parse("2010-07-16"), 148, true, null),
            new Film(2, "Memento",   "Thriller", LocalDate.parse("2000-10-11"), 113, false, null)
        );
        when(filmRepository.findAll()).thenReturn(mockFilms);

        var result = filmService.getAllFilms(null);
        assertEquals(2, result.size());
        assertEquals("Inception", result.get(0).getTitle());

        verify(filmRepository).findAll();
        verify(filmRepository, never()).findByGenre(anyString());
    }

    @Test
    void testGetAllFilmsByGenre() {
        List<Film> mockFilms = List.of(
            new Film(1, "Inception", "Sci-Fi", LocalDate.parse("2010-07-16"), 148, true, null)
        );
        when(filmRepository.findByGenre("Sci-Fi")).thenReturn(mockFilms);

        var result = filmService.getAllFilms("Sci-Fi");
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());

        verify(filmRepository).findByGenre("Sci-Fi");
        verify(filmRepository, never()).findAll();
    }

    @Test
    void testGetFilmByIdExists() {
        Film mockFilm = new Film(1, "Inception", "Sci-Fi", LocalDate.parse("2010-07-16"), 148, true, null);
        when(filmRepository.findById(1)).thenReturn(Optional.of(mockFilm));

        var result = filmService.getFilmById(1);
        assertTrue(result.isPresent());
        assertEquals("Inception", result.get().getTitle());

        verify(filmRepository).findById(1);
    }

    @Test
    void testCreateFilm() {
        Film input = new Film(null, "Interstellar", "Sci-Fi", LocalDate.parse("2014-11-07"), 169, true, null);
        Film saved = new Film(3, input.getTitle(), input.getGenre(), input.getReleaseDate(), input.getDuration(), input.isViewed(), null);
        when(filmRepository.save(input)).thenReturn(saved);

        var result = filmService.createFilm(input);
        assertEquals(3, result.getId());
        assertEquals("Interstellar", result.getTitle());

        verify(filmRepository).save(input);
    }

    @Test
    void testUpdateFilm() {
        long id = 1;
        Film input = new Film(id, "Inception Updated", "Sci-Fi", LocalDate.parse("2010-07-16"), 148, true, null);
        when(filmRepository.existsById(id)).thenReturn(true);
        when(filmRepository.save(input)).thenReturn(input);

        var result = filmService.updateFilm(id, input);
        assertEquals("Inception Updated", result.getTitle());

        verify(filmRepository).existsById(id);
        verify(filmRepository).save(input);
    }

    @Test
    void testUpdateFilmNotFound() {
        long id = 99;
        Film input = new Film(id, "Nonexistent", "Drama", LocalDate.parse("2020-01-01"), 120, false, null);
        when(filmRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> filmService.updateFilm(id, input));

        verify(filmRepository).existsById(id);
        verify(filmRepository, never()).save(any(Film.class));
    }

    @Test
    void testPartialUpdateFilm() {
        long id = 1;
        Film existing = new Film(id, "Original", "Sci-Fi", LocalDate.parse("2010-07-16"), 148, true, null);
        Film partial = new Film();
        partial.setTitle("Updated Title");

        when(filmRepository.findById(id)).thenReturn(Optional.of(existing));
        // para el save, devolvemos lo que nos pasan
        when(filmRepository.save(any(Film.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = filmService.partialUpdateFilm(id, partial);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Sci-Fi", result.getGenre()); // lo que no se cambió

        verify(filmRepository).findById(id);
        verify(filmRepository).save(any(Film.class));
    }

    @Test
    void testPartialUpdateFilmNotFound() {
        long id = 99;
        Film partial = new Film();
        partial.setTitle("Won't apply");

        when(filmRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> filmService.partialUpdateFilm(id, partial));

        verify(filmRepository).findById(id);
        verify(filmRepository, never()).save(any(Film.class));
    }

    @Test
    void testDeleteFilm() {
        long id = 1;
        // si no lanza excepción, entendemos que borró
        filmService.deleteFilm(id);
        verify(filmRepository).deleteById(id);
    }
}

public class FilmServiceTests {
}
