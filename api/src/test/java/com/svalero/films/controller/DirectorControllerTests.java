package com.svalero.films.controller;

import com.svalero.films.config.TestSecurityConfig;
import com.svalero.films.domain.Director;
import com.svalero.films.exception.ResourceNotFoundException;
import com.svalero.films.security.JwtTokenService;
import com.svalero.films.service.DirectorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DirectorController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class DirectorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtTokenService jwtTokenService;

    @MockBean
    private DirectorService directorService;

    @Test
    void testGetAllDirectors_returnOk() throws Exception {
        List<Director> directors = List.of(
            new Director(1L, "Quentin", "Tarantino", LocalDate.parse("1963-03-27"), "American", true)
        );
        when(directorService.getAllDirectors("")).thenReturn(directors);

        mockMvc.perform(get("/directors"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].name").value("Quentin"));
    }

    @Test
    void testGetAllDirectors_withGenre_returnNotFound() throws Exception {
        when(directorService.getAllDirectors("Action"))
            .thenThrow(new ResourceNotFoundException("No directors found for films in genre: Action"));

        mockMvc.perform(get("/directors").param("genre", "Action"))
            .andExpect(status().isNotFound())
            .andExpect(content().json("[]"));
    }

    @Test
    void testGetDirectorById_returnOk() throws Exception {
        Director d = new Director(2L, "Christopher", "Nolan", LocalDate.parse("1970-07-30"), "British-American", true);
        when(directorService.getDirectorsById(2L)).thenReturn(d);

        mockMvc.perform(get("/directors/{id}", 2L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lastName").value("Nolan"));
    }

    @Test
    void testGetDirectorById_invalidFormat() throws Exception {
        mockMvc.perform(get("/directors/abc"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateDirector_returnCreated() throws Exception {
        Director in = new Director(null, "Patty", "Jenkins", LocalDate.parse("1971-07-24"), "American", false);
        Director out = new Director(5L, "Patty", "Jenkins", in.getBirthDate(), "American", false);
        when(directorService.createDirectors(in)).thenReturn(out);

        mockMvc.perform(post("/directors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(in)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void testCreateDirector_returnBadRequest_onIllegalArgument() throws Exception {
        Director bad = new Director();
        when(directorService.createDirectors(bad))
            .thenThrow(new IllegalArgumentException("Name cannot be null or empty"));

        mockMvc.perform(post("/directors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bad)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateDirector_returnOk() throws Exception {
        Director in = new Director(null, "Greta", "Gerwig", LocalDate.parse("1983-08-04"), "American", false);
        Director out = new Director(3L, "Greta", "Gerwig", in.getBirthDate(), "American", false);
        when(directorService.updateDirector(3L, in)).thenReturn(out);

        mockMvc.perform(put("/directors/{id}", 3L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(in)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Greta"));
    }

    @Test
    void testUpdateDirector_returnNotFound() throws Exception {
        Director any = new Director();
        when(directorService.updateDirector(7L, any))
            .thenThrow(new ResourceNotFoundException("Director with ID 7 not found"));

        mockMvc.perform(put("/directors/{id}", 7L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(any)))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateDirector_returnBadRequest_onIllegalArgument() throws Exception {
        Director invalid = new Director(null, "", "Last", null, null, null);
        when(directorService.updateDirector(4L, invalid))
            .thenThrow(new IllegalArgumentException("Last Name cannot be null or empty"));

        mockMvc.perform(put("/directors/{id}", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testPatchDirector_returnOk() throws Exception {
        Director patch = new Director();
        patch.setNationality("Canadian");
        Director patched = new Director(6L, "James", "Cameron", LocalDate.parse("1954-08-16"), "Canadian", true);
        when(directorService.updatePartialDirector(6L, patch)).thenReturn(patched);

        mockMvc.perform(patch("/directors/{id}", 6L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nationality").value("Canadian"));
    }

    @Test
    void testPatchDirector_returnNotFound() throws Exception {
        Director patch = new Director();
        patch.setName("X");
        when(directorService.updatePartialDirector(9L, patch))
            .thenThrow(new ResourceNotFoundException("Director not found"));

        mockMvc.perform(patch("/directors/{id}", 9L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch)))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteDirector_returnNoContent() throws Exception {
        mockMvc.perform(delete("/directors/{id}", 1L))
            .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteDirector_returnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Director not found"))
            .when(directorService).deleteDirector(8L);

        mockMvc.perform(delete("/directors/{id}", 8L))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteDirector_invalidFormat() throws Exception {
        mockMvc.perform(delete("/directors/abc"))
            .andExpect(status().isBadRequest());
    }

}
