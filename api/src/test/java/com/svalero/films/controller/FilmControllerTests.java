// src/test/java/com/example/filmsapi/controller/FilmControllerTests.java
package com.svalero.films.controller;

import com.svalero.films.domain.Film;
import com.svalero.films.exception.ErrorResponse;
import com.svalero.films.service.FilmService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmController.class)
public class FilmControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FilmService filmService;

    @Test
    public void testGetAllFilmsNoParamsReturnOK() throws Exception {
        List<Film> mockFilms = List.of(
            new Film(1, "Inception", "Sci-Fi", LocalDate.parse("2010-07-16"), 148, true, null),
            new Film(2, "Memento",   "Thriller", LocalDate.parse("2000-10-11"), 113, false, null)
        );
        when(filmService.getAllFilms(null)).thenReturn(mockFilms);

        var mvcResult = mockMvc.perform(get("/films"))
            .andExpect(status().isOk())
            .andReturn();

        var films = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            new TypeReference<List<Film>>() {}
        );
        assertEquals(2, films.size());
        assertEquals("Inception", films.get(0).getTitle());
    }

    @Test
    public void testGetAllFilmsWithGenreReturnOK() throws Exception {
        List<Film> mockFilms = List.of(
            new Film(1, "Inception", "Sci-Fi", LocalDate.parse("2010-07-16"), 148, true, null)
        );
        when(filmService.getAllFilms("Sci-Fi")).thenReturn(mockFilms);

        var mvcResult = mockMvc.perform(get("/films").param("genre", "Sci-Fi"))
            .andExpect(status().isOk())
            .andReturn();

        var films = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            new TypeReference<List<Film>>() {}
        );
        assertEquals(1, films.size());
        assertEquals("Inception", films.get(0).getTitle());
    }

    @Test
    public void testGetAllFilmsInternalServerError() throws Exception {
        when(filmService.getAllFilms(null)).thenThrow(new RuntimeException("DB error"));

        var mvcResult = mockMvc.perform(get("/films"))
            .andExpect(status().isInternalServerError())
            .andReturn();

        var error = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            ErrorResponse.class
        );
        assertEquals(500, error.getStatus());
    }

    @Test
    public void testGetFilmByIdReturnOK() throws Exception {
        Film mockFilm = new Film(1, "Inception", "Sci-Fi", LocalDate.parse("2010-07-16"), 148, true, null);
        when(filmService.getFilmById(1)).thenReturn(Optional.of(mockFilm));

        var mvcResult = mockMvc.perform(get("/films/1"))
            .andExpect(status().isOk())
            .andReturn();

        var film = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            Film.class
        );
        assertEquals("Inception", film.getTitle());
    }

    @Test
    public void testGetFilmByIdReturnInternalServerError() throws Exception {
        var mvcResult = mockMvc.perform(get("/films/abc"))
            .andExpect(status().isInternalServerError())
            .andReturn();

        var error = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            ErrorResponse.class
        );
        assertEquals(500, error.getStatus());
        assertEquals("MethodArgumentTypeMismatchException", error.getError());
    }

    @Test
    public void testGetFilmByIdReturnNotFound() throws Exception {
        when(filmService.getFilmById(1)).thenReturn(Optional.empty());

        var mvcResult = mockMvc.perform(get("/films/1"))
            .andExpect(status().isNotFound())
            .andReturn();

        var error = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            ErrorResponse.class
        );
        assertEquals(404, error.getStatus());
        assertEquals("EntityNotFoundException", error.getError());
    }

    @Test
    public void testCreateFilmReturnCreated() throws Exception {
        Film input = new Film(null, "Dunkirk", "War", LocalDate.parse("2017-07-21"), 106, false, null);
        Film saved = new Film(3, input.getTitle(), input.getGenre(), input.getReleaseDate(), input.getDuration(), input.isViewed(), null);
        when(filmService.createFilm(input)).thenReturn(saved);

        var mvcResult = mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isCreated())
            .andReturn();

        var film = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            Film.class
        );
        assertEquals(3, film.getId());
    }

    @Test
    public void testCreateFilmReturnBadRequest() throws Exception {
        Film invalid = new Film();
        var mvcResult = mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(invalid)))
            .andExpect(status().isBadRequest())
            .andReturn();

        var error = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            ErrorResponse.class
        );
        assertEquals(400, error.getStatus());
    }

    @Test
    public void testCreateFilmReturnInternalServerError() throws Exception {
        Film input = new Film(null, "Dunkirk", "War", LocalDate.parse("2017-07-21"), 106, false, null);
        when(filmService.createFilm(input)).thenThrow(new RuntimeException("DB error"));

        var mvcResult = mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isInternalServerError())
            .andReturn();

        var error = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            ErrorResponse.class
        );
        assertEquals(500, error.getStatus());
    }

    @Test
    public void testUpdateFilmReturnOK() throws Exception {
        Film input = new Film(null, "Inception Upt", "Sci-Fi", LocalDate.parse("2010-07-16"), 148, true, null);
        Film updated = new Film(1, input.getTitle(), input.getGenre(), input.getReleaseDate(), input.getDuration(), input.isViewed(), null);
        when(filmService.updateFilm(1, input)).thenReturn(updated);

        var mvcResult = mockMvc.perform(put("/films/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andReturn();

        var film = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            Film.class
        );
        assertEquals("Inception Upt", film.getTitle());
    }

    @Test
    public void testUpdateFilmReturnNotFound() throws Exception {
        Film input = new Film(null, "Ghost", "Horror", LocalDate.parse("2021-10-31"), 100, false, null);
        when(filmService.updateFilm(1, input)).thenThrow(new EntityNotFoundException("Film not found"));

        var mvcResult = mockMvc.perform(put("/films/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isNotFound())
            .andReturn();

        var error = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            ErrorResponse.class
        );
        assertEquals(404, error.getStatus());
    }

    @Test
    public void testUpdateFilmReturnBadRequest() throws Exception {
        Film invalid = new Film();
        var mvcResult = mockMvc.perform(put("/films/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(invalid)))
            .andExpect(status().isBadRequest())
            .andReturn();

        var error = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            ErrorResponse.class
        );
        assertEquals(400, error.getStatus());
    }

    @Test
    public void testPartialUpdateFilmReturnOK() throws Exception {
        Film input = new Film(); input.setTitle("Patched Title");
        Film patched = new Film(1, "Patched Title", "Sci-Fi", LocalDate.parse("2010-07-16"), 148, true, null);
        when(filmService.partialUpdateFilm(1, input)).thenReturn(patched);

        var mvcResult = mockMvc.perform(patch("/films/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andReturn();

        var film = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            Film.class
        );
        assertEquals("Patched Title", film.getTitle());
    }

    @Test
    public void testPartialUpdateFilmReturnNotFound() throws Exception {
        Film input = new Film(); input.setTitle("Patched");
        when(filmService.partialUpdateFilm(1, input)).thenThrow(new EntityNotFoundException("Film not found"));

        var mvcResult = mockMvc.perform(patch("/films/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isNotFound())
            .andReturn();

        var error = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            ErrorResponse.class
        );
        assertEquals(404, error.getStatus());
    }

    @Test
    public void testDeleteFilmReturnNoContent() throws Exception {
        mockMvc.perform(delete("/films/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteFilmReturnNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Film not found")).when(filmService).deleteFilm(1);

        var mvcResult = mockMvc.perform(delete("/films/1"))
            .andExpect(status().isNotFound())
            .andReturn();

        var error = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            ErrorResponse.class
        );
        assertEquals(404, error.getStatus());
    }

    @Test
    public void testDeleteFilmReturnInternalServerError() throws Exception {
        doThrow(new RuntimeException("DB error")).when(filmService).deleteFilm(1);

        var mvcResult = mockMvc.perform(delete("/films/1"))
            .andExpect(status().isInternalServerError())
            .andReturn();

        var error = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            ErrorResponse.class
        );
        assertEquals(500, error.getStatus());
    }
}
