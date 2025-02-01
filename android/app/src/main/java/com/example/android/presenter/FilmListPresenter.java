package com.example.android.presenter;

import com.example.android.contract.FilmListContract;
import com.example.android.domain.Film;
import com.example.android.model.FilmListModel;

import java.util.List;

public class FilmListPresenter implements FilmListContract.Presenter, FilmListContract.Model.OnLoadFilmsListener {

    private final FilmListContract.View view;
    private final FilmListContract.Model model;

    public FilmListPresenter(FilmListContract.View view) {
        this.view = view;
        this.model = new FilmListModel();
    }

    @Override
    public void loadFilms() {
        model.loadFilms(this);
    }

    @Override
    public void onLoadFilmsSuccess(List<Film> filmList) {
        view.listFilms(filmList);
    }

    @Override
    public void onLoadFilmsError(String message) {
        view.showErrorMessage(message);
    }

}
