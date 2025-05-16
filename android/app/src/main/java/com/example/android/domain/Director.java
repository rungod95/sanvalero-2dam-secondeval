package com.example.android.domain;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Director {
    private Long id;
    private String name;
    private String lastName;
    private LocalDate birthDate;
    private String nationality;
    private boolean awarded;
}
