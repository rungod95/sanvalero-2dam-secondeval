
package com.svalero.films.controller;

import com.svalero.films.domain.Director;
import com.svalero.films.domain.Film;
import com.svalero.films.exception.ResourceNotFoundException;
import com.svalero.films.security.JwtAuthenticationFilter;
import com.svalero.films.security.JwtTokenService;
import com.svalero.films.service.FilmService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")

class FilmControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtTokenService jwtTokenService;


    @MockBean
    private FilmService filmService;


    @Test
    void testGetAllFilms_returnOk() throws Exception {
        List<Film> films = List.of(
            new Film(1L, "Inception", "Sci-Fi", LocalDate.parse("2010-07-16"), 148, true, null)
        );
        when(filmService.getAllFilms()).thenReturn(films);

        mockMvc.perform(get("/films"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].title").value("Inception"));
    }

    @Test
    void testGetFilmById_returnOk() throws Exception {
        Film film = new Film(1L, "Inception", "Sci-Fi", LocalDate.parse("2010-07-16"), 148, true, null);
        when(filmService.getFilmsById(1L)).thenReturn(film);

        mockMvc.perform(get("/films/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetFilmById_returnNotFound() throws Exception {
        when(filmService.getFilmsById(2L))
            .thenThrow(new ResourceNotFoundException("Film not found"));

        mockMvc.perform(get("/films/{id}", 2L))
            .andExpect(status().isNotFound());
    }

    @Test
    void testGetFilmById_invalidFormat() throws Exception {
        mockMvc.perform(get("/films/abc"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateFilm_returnCreated() throws Exception {
        Director d = new Director(1L, "Name", "Last", LocalDate.parse("1970-01-01"), "Nat", false);
        Film in = new Film(null, "Dunkirk", "War", LocalDate.parse("2017-07-21"), 106, false, d);
        Film out = new Film(5L, "Dunkirk", "War", in.getReleaseDate(), 106, false, d);

        when(filmService.createFilms(in)).thenReturn(out);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(in)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void testCreateFilm_returnBadRequest_onIllegalArgument() throws Exception {
        Film bad = new Film();
        when(filmService.createFilms(bad))
            .thenThrow(new IllegalArgumentException("Title cannot be null or empty"));

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bad)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateFilm_returnNotFound_onDirectorMissing() throws Exception {
        Director d = new Director(2L, null, null, null, null, null);
        Film in = new Film(null, "T", "G", LocalDate.now(), 100, true, d);

        when(filmService.createFilms(in))
            .thenThrow(new ResourceNotFoundException("Director with ID 2 not found"));

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(in)))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateFilm_returnOk() throws Exception {
        Director d = new Director(1L, "N", "L", LocalDate.parse("1970-01-01"), "Nat", false);
        Film in = new Film(null, "Updated", "G", LocalDate.parse("2010-07-16"), 148, true, d);
        Film out = new Film(1L, "Updated", "G", in.getReleaseDate(), 148, true, d);

        when(filmService.updateFilm(1L, in)).thenReturn(out);

        mockMvc.perform(put("/films/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(in)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void testUpdateFilm_returnNotFound() throws Exception {
        when(filmService.updateFilm(3L, new Film()))
            .thenThrow(new ResourceNotFoundException("Film with ID 3 not found"));

        mockMvc.perform(put("/films/{id}", 3L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Film())))
            .andExpect(status().isNotFound());
    }

    @Test
    void testPatchFilm_returnOk() throws Exception {
        Film patch = new Film(); patch.setTitle("Patched");
        Film patched = new Film(1L, "Patched", "G", LocalDate.parse("2010-07-16"), 100, false, null);

        when(filmService.updatePartialFilm(1L, patch)).thenReturn(patched);

        mockMvc.perform(patch("/films/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Patched"));
    }

    @Test
    void testPatchFilm_returnNotFound() throws Exception {
        Film patch = new Film(); patch.setTitle("X");
        when(filmService.updatePartialFilm(4L, patch))
            .thenThrow(new ResourceNotFoundException("Film not found"));

        mockMvc.perform(patch("/films/{id}", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch)))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteFilm_returnNoContent() throws Exception {
        mockMvc.perform(delete("/films/{id}", 1L))
            .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteFilm_returnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Film not found"))
            .when(filmService).deleteFilm(5L);

        mockMvc.perform(delete("/films/{id}", 5L))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteFilm_invalidFormat() throws Exception {
        mockMvc.perform(delete("/films/abc"))
            .andExpect(status().isBadRequest());
    }
}
