package com.example.android.contract;

import com.example.android.domain.Director;
import com.example.android.domain.Film;

public interface RegisterFilmContract {

    interface View {
        void showSuccessMessage(String message);
        void showErrorMessage(String message);
    }

    interface Presenter {
        void saveFilm(String title, String genre, String releaseDate,
                      int duration, boolean viewed, Director director);
        void editFilm(long filmId, String title, String genre, String releaseDate,
                      int duration, boolean viewed, Director director);
        void fetchFilmDetails(long filmId, Model.OnFetchFilmListener listener);
    }

    interface Model {
        void saveFilm(Film film, OnSaveFilmListener listener);
        void editFilm(long filmId, Film film, OnSaveFilmListener listener);
        void fetchFilmDetails(long filmId, OnFetchFilmListener listener);

        interface OnSaveFilmListener {
            void onSuccess(String message);
            void onError(String message);
        }

        interface OnFetchFilmListener {
            void onSuccess(Film film);
            void onError(String message);
        }
    }



}
