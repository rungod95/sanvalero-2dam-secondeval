package com.svalero.javaFX.model;

import lombok.Data;

@Data
public class Director {
    private int id;
    private String name;
    private String lastName;
    private String nationality;
    private boolean awarded;
}
