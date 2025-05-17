package com.svalero.javaFX.model;

import lombok.Data;

@Data
public class Film {
    private int id;
    private String title;
    private String genre;
    private int duration;
    private boolean viewed;
    private Director director;
}
