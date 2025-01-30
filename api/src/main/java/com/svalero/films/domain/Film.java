package com.svalero.films.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import com.svalero.films.domain.Director;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Film")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false, name = "release_date")
    private LocalDate releaseDate;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private Boolean viewed;

    @ManyToOne
    @JoinColumn(name = "director_id")
    @JsonBackReference
    private Director director;

}






