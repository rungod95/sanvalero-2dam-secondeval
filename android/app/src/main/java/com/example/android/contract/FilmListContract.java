package com.example.android.contract;

import com.example.android.domain.Film;

import java.util.List;

public interface FilmListContract {
    interface Model {
        interface OnLoadFilmsListener {
            void onLoadFilmsSuccess(List<Film> filmList);
            void onLoadFilmsError(String message);
        }
        void loadFilms(FilmListContract.Model.OnLoadFilmsListener listener);
    }

    interface View {

        void listFilms(List<Film> filmList);
        void showErrorMessage(String message);
        void showSuccessMessage(String message);
    }

    interface Presenter {
        void loadFilms();
    }
}
