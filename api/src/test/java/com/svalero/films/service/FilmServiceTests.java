package com.svalero.films.service;

import com.svalero.films.domain.Director;
import com.svalero.films.domain.Film;
import com.svalero.films.exception.ResourceNotFoundException;
import com.svalero.films.repository.DirectorRepository;
import com.svalero.films.repository.FilmRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmServiceTests {

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private DirectorRepository directorRepository;

    @InjectMocks
    private FilmService filmService;

    @Test
    void whenGetAllFilms_thenReturnList() {
        List<Film> films = List.of(
            new Film(1L, "Inception", "Sci-Fi", LocalDate.parse("2010-07-16"), 148, true, null)
        );
        when(filmRepository.findAll()).thenReturn(films);

        List<Film> result = filmService.getAllFilms();

        assertThat(result)
            .hasSize(1)
            .first()
            .extracting(Film::getTitle)
            .isEqualTo("Inception");
        verify(filmRepository).findAll();
    }

    @Test
    void whenGetFilmsById_exists_thenReturnFilm() {
        Film film = new Film(1L, "Inception", "Sci-Fi", LocalDate.parse("2010-07-16"), 148, true, null);
        when(filmRepository.findById(1L)).thenReturn(Optional.of(film));

        Film result = filmService.getFilmsById(1L);

        assertThat(result).isEqualTo(film);
        verify(filmRepository).findById(1L);
    }

    @Test
    void whenGetFilmsById_notExists_thenThrow() {
        when(filmRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> filmService.getFilmsById(99L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Film not found");
    }

    @Test
    void whenCreateFilms_null_thenThrow() {
        assertThatThrownBy(() -> filmService.createFilms(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Film cannot be null");
    }

    @Test
    void whenCreateFilms_missingTitle_thenThrow() {
        Film bad = new Film(null, "", "Genre", LocalDate.now(), 120, true,
            new Director(1L, "Name", "Last", LocalDate.now(), "Nat", false));
        assertThatThrownBy(() -> filmService.createFilms(bad))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Title cannot be null or empty");
    }

    @Test
    void whenCreateFilms_directorNotFound_thenThrow() {
        Film f = new Film(null, "T", "G", LocalDate.now(), 90, false,
            new Director(2L, "X", "Y", LocalDate.now(), "Z", true));
        when(directorRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> filmService.createFilms(f))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Director with ID 2 not found");
    }

    @Test
    void whenCreateFilms_valid_thenReturnSaved() {
        Director d = new Director(1L, "N", "L", LocalDate.parse("1970-01-01"), "Nat", false);
        Film in = new Film(null, "T", "G", LocalDate.now(), 100, true, d);
        Film out = new Film(5L, "T", "G", in.getReleaseDate(), 100, true, d);

        when(directorRepository.findById(1L)).thenReturn(Optional.of(d));
        when(filmRepository.save(in)).thenReturn(out);

        Film result = filmService.createFilms(in);

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getDirector()).isEqualTo(d);
        verify(filmRepository).save(in);
    }

    @Test
    void whenUpdateFilm_exists_thenReturnUpdated() {
        Long id = 1L;
        Film existing = new Film(id, "Old", "G", LocalDate.now(), 80, false, null);
        Film updated = new Film(id, "New", "G", existing.getReleaseDate(), 80, true, null);

        when(filmRepository.findById(id)).thenReturn(Optional.of(existing));
        when(filmRepository.save(existing)).thenReturn(updated);

        Film result = filmService.updateFilm(id, updated);

        assertThat(result.getTitle()).isEqualTo("New");
        verify(filmRepository).findById(id);
        verify(filmRepository).save(existing);
    }

    @Test
    void whenUpdateFilm_notFound_thenThrow() {
        when(filmRepository.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> filmService.updateFilm(3L, new Film()))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void whenUpdatePartialFilm_exists_thenReturnUpdated() {
        Long id = 1L;
        Film existing = new Film(id, "Old", "G", LocalDate.now(), 80, false, null);
        Film partial = new Film(); partial.setTitle("Part");

        when(filmRepository.findById(id)).thenReturn(Optional.of(existing));
        when(filmRepository.save(existing)).thenReturn(existing);

        Film result = filmService.updatePartialFilm(id, partial);

        assertThat(result.getTitle()).isEqualTo("Part");
    }

    @Test
    void whenUpdatePartialFilm_notFound_thenThrow() {
        when(filmRepository.findById(4L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> filmService.updatePartialFilm(4L, new Film()))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void whenDeleteFilm_exists_thenDeleted() {
        Long id = 1L;
        Film f = new Film(id, "T", "G", LocalDate.now(), 70, false, null);
        when(filmRepository.findById(id)).thenReturn(Optional.of(f));

        filmService.deleteFilm(id);

        verify(filmRepository).delete(f);
    }

    @Test
    void whenDeleteFilm_notFound_thenThrow() {
        when(filmRepository.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> filmService.deleteFilm(7L))
            .isInstanceOf(ResourceNotFoundException.class);
    }
}
