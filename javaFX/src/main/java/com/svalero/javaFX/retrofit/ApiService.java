package com.svalero.javaFX.retrofit;

import java.util.List;

import com.svalero.javaFX.model.Director;
import com.svalero.javaFX.model.Film;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiService {
    @GET("films")
    Observable<List<Film>> getAllFilms(); // Devuelve un Observable

    @GET("directors")
    Observable<List<Director>> getAllDirectors(); // Devuelve un Observable
}