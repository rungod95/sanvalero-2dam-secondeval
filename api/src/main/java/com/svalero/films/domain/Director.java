package com.svalero.films.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "directors")
//@Table(name = "directors")
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name="last_name")
    private String lastName;

    @Column(nullable = false, name="birth_date")
    private LocalDate birthDate;

    @Column(nullable = false)
    private String nationality;

    @Column(nullable = false)
    private Boolean awarded;
}
