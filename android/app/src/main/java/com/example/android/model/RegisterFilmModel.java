package com.example.android.model;

import com.example.android.api.FilmApi;
import com.example.android.api.FilmApiInterface;
import com.example.android.contract.RegisterFilmContract;
import com.example.android.contract.RegisterFilmContract.Model.OnSaveFilmListener;
import com.example.android.contract.RegisterFilmContract.Model.OnFetchFilmListener;
import com.example.android.domain.Film;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFilmModel implements RegisterFilmContract.Model {

    private final FilmApiInterface apiService;

    public RegisterFilmModel() {
        this.apiService = FilmApi.buildInstance();
    }

    @Override
    public void saveFilm(Film film, OnSaveFilmListener listener) {
        apiService.createFilm(film).enqueue(new Callback<Film>() {
            @Override
            public void onResponse(Call<Film> call, Response<Film> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess("Film created successfully.");
                } else {
                    listener.onError("Failed to create film. Code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Film> call, Throwable t) {
                listener.onError("API error: " + t.getMessage());
            }
        });
    }

    @Override
    public void editFilm(long filmId, Film film, OnSaveFilmListener listener) {
        apiService.updateFilm(filmId, film).enqueue(new Callback<Film>() {
            @Override
            public void onResponse(Call<Film> call, Response<Film> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess("Film updated successfully.");
                } else {
                    listener.onError("Failed to update film. Code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Film> call, Throwable t) {
                listener.onError("API error: " + t.getMessage());
            }
        });
    }

    @Override
    public void fetchFilmDetails(long filmId, OnFetchFilmListener listener) {
        apiService.getFilm(filmId).enqueue(new Callback<Film>() {
            @Override
            public void onResponse(Call<Film> call, Response<Film> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("Failed to fetch film. Code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Film> call, Throwable t) {
                listener.onError("API error: " + t.getMessage());
            }
        });
    }
}
