package com.example.android.api;




import com.example.android.domain.Film;

import java.util.List;



import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface FilmApiInterface {

    //Obtener las peliculas
    @GET("films")
    Call<List<Film>> getFilms();

    //Obtener detalles de una pelicula por ID
    @GET("films/{id}")
    Call<Film> getFilm(@Path("id") long id);

    //Crear una nueva pelicula
    @POST("films")
    Call<Film> createFilm(@Body Film film);

    //Actualizar una pelicula existente
    @PUT("films/{id}")
    Call<Film> updateFilm(@Path("id") long id, @Body Film film);


    //Eliminar una pelicula por ID
    @DELETE("films/{id}")
    Call<Void> deleteFilm(@Path("id") long id);
}
