package com.example.android.presenter;

import android.util.Log;

import com.example.android.contract.RegisterFilmContract;
import com.example.android.domain.Director;
import com.example.android.domain.Film;
import com.example.android.model.RegisterFilmModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.time.LocalDate;

public class RegisterFilmPresenter implements RegisterFilmContract.Presenter {

    private final RegisterFilmContract.View view;
    private final RegisterFilmContract.Model model;

    public RegisterFilmPresenter(RegisterFilmContract.View view) {
        this.view = view;
        this.model = new RegisterFilmModel();
    }
    @Override
    public void saveFilm(String title, String genre, String releaseDate,
                         int duration, boolean viewed, Director director) {
        Film film = new Film(
            null,
            title,
            genre,
            LocalDate.parse(releaseDate),
            duration,
            viewed,
            director             // ← director con TODOS sus campos
        );
        model.saveFilm(film, crearListener());
    }





    @Override
    public void editFilm(long filmId, String title, String genre, String releaseDate,
                         int duration, boolean viewed, Director director) {
        Film film = new Film(
            filmId,
            title,
            genre,
            LocalDate.parse(releaseDate),
            duration,
            viewed,
            director
        );

        // ¡Aquí logueamos el JSON!
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>)(src, typeOfSrc, ctx) ->
                new JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)(json, type, ctx) ->
                LocalDate.parse(json.getAsString()))
            .setDateFormat("yyyy-MM-dd")
            .create();
        Log.d("DEBUG_PUT_BODY", gson.toJson(film));

        model.editFilm(filmId, film, crearListener());
    }





    private RegisterFilmContract.Model.OnSaveFilmListener crearListener() {
        return new RegisterFilmContract.Model.OnSaveFilmListener() {
            @Override
            public void onSuccess(String message) {
                view.showSuccessMessage(message);
            }
            @Override
            public void onError(String message) {
                view.showErrorMessage(message);
            }
        };
    }

    @Override
    public void fetchFilmDetails(long filmId, RegisterFilmContract.Model.OnFetchFilmListener listener) {
        model.fetchFilmDetails(filmId, listener);
    }
}
