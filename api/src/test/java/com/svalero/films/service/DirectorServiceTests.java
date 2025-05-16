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
class DirectorServiceTests {

    @Mock
    private DirectorRepository directorRepository;

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private DirectorService directorService;


    @Test
    void whenGetAllDirectors_noGenre_thenReturnAll() {
        List<Director> directors = List.of(
            new Director(1L, "A", "B", LocalDate.of(1970,1,1), "X", true),
            new Director(2L, "C", "D", LocalDate.of(1980,2,2), "Y", false)
        );
        when(directorRepository.findAll()).thenReturn(directors);

        var result = directorService.getAllDirectors("");

        assertThat(result).hasSize(2).containsExactlyElementsOf(directors);
        verify(directorRepository).findAll();
    }


    @Test
    void whenGetAllDirectors_withGenre_thenReturnDistinctDirectors() {
        Director d1 = new Director(1L, "A", "B", LocalDate.now(), "X", true);
        Director d2 = new Director(2L, "C", "D", LocalDate.now(), "Y", false);
        Film f1 = new Film(1L, "F1", "Action", LocalDate.now(), 120, true, d1);
        Film f2 = new Film(2L, "F2", "Action", LocalDate.now(), 90, false, d2);
        when(filmRepository.findByGenre("Action")).thenReturn(List.of(f1, f2));

        var result = directorService.getAllDirectors("Action");

        assertThat(result).containsExactlyInAnyOrder(d1, d2);
        verify(filmRepository).findByGenre("Action");
    }


    @Test
    void whenGetAllDirectors_withGenreNoMatches_thenThrow() {
        when(filmRepository.findByGenre("Drama")).thenReturn(List.of());

        assertThatThrownBy(() -> directorService.getAllDirectors("Drama"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("No directors found for films in genre: Drama");
    }


    @Test
    void whenGetDirectorById_exists_thenReturn() {
        Director d = new Director(3L, "E", "F", LocalDate.of(1975,5,5), "Z", true);
        when(directorRepository.findById(3L)).thenReturn(Optional.of(d));

        var result = directorService.getDirectorsById(3L);

        assertThat(result).isEqualTo(d);
        verify(directorRepository).findById(3L);
    }


    @Test
    void whenGetDirectorById_notExists_thenThrow() {
        when(directorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> directorService.getDirectorsById(99L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Director not found");
    }


    @Test
    void whenCreateDirector_null_thenThrow() {
        assertThatThrownBy(() -> directorService.createDirectors(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Director cannot be null");
    }


    @Test
    void whenCreateDirector_missingName_thenThrow() {
        Director bad = new Director(null, "", "L", LocalDate.now(), "N", true);
        assertThatThrownBy(() -> directorService.createDirectors(bad))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Name cannot be null or empty");
    }

    @Test
    void whenCreateDirector_missingLastName_thenThrow() {
        Director bad = new Director(null, "N", "", LocalDate.now(), "X", false);
        assertThatThrownBy(() -> directorService.createDirectors(bad))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Last Name cannot be null or empty");
    }

    @Test
    void whenCreateDirector_valid_thenReturnSaved() {
        Director in = new Director(null, "G", "H", LocalDate.of(1985,8,8), "Q", true);
        Director out = new Director(5L, "G", "H", in.getBirthDate(), "Q", true);
        when(directorRepository.save(in)).thenReturn(out);

        var result = directorService.createDirectors(in);

        assertThat(result.getId()).isEqualTo(5L);
        verify(directorRepository).save(in);
    }


    @Test
    void whenUpdateDirector_existsAndValid_thenReturnUpdated() {
        Long id = 10L;
        Director existing = new Director(id, "Old", "X", LocalDate.now(), "P", false);
        Director updated = new Director(id, "New", "Y", LocalDate.of(1990,9,9), "R", true);

        when(directorRepository.findById(id)).thenReturn(Optional.of(existing));
        when(directorRepository.save(existing)).thenReturn(updated);

        var result = directorService.updateDirector(id, updated);

        assertThat(result.getName()).isEqualTo("New");
        verify(directorRepository).findById(id);
        verify(directorRepository).save(existing);
    }


    @Test
    void whenUpdateDirector_notFound_thenThrow() {
        when(directorRepository.findById(11L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> directorService.updateDirector(11L, new Director()))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Director with ID 11 not found");
    }


    @Test
    void whenUpdatePartialDirector_exists_thenApplyPatches() {
        Long id = 20L;
        Director existing = new Director(id, "A", "B", LocalDate.of(1970,1,1), "M", true);
        Director patch = new Director(); patch.setNationality("Canadian");

        when(directorRepository.findById(id)).thenReturn(Optional.of(existing));
        when(directorRepository.save(existing)).thenReturn(existing);

        var result = directorService.updatePartialDirector(id, patch);

        assertThat(result.getNationality()).isEqualTo("Canadian");
        verify(directorRepository).findById(id);
        verify(directorRepository).save(existing);
    }


    @Test
    void whenUpdatePartialDirector_notFound_thenThrow() {
        when(directorRepository.findById(30L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> directorService.updatePartialDirector(30L, new Director()))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Director not found");
    }


    @Test
    void whenDeleteDirector_exists_thenDelete() {
        Long id = 40L;
        Director d = new Director(id, "Z", "W", LocalDate.now(), "U", false);
        when(directorRepository.findById(id)).thenReturn(Optional.of(d));

        directorService.deleteDirector(id);

        verify(directorRepository).delete(d);
    }


    @Test
    void whenDeleteDirector_notFound_thenThrow() {
        when(directorRepository.findById(50L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> directorService.deleteDirector(50L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Director with ID 50 not found");
    }
}
