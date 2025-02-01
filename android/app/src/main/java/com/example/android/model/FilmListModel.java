package com.example.android.model;

import com.example.android.api.FilmApi;
import com.example.android.api.FilmApiInterface;
import com.example.android.contract.FilmListContract;
import com.example.android.domain.Film;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmListModel implements FilmListContract.Model {
    @Override
    public void loadFilms(FilmListContract.Model.OnLoadFilmsListener listener) {
        FilmApiInterface filmApi = FilmApi.buildInstance();
        Call<List<Film>> getFilmsCall = filmApi.getFilms();

        getFilmsCall.enqueue(new Callback<List<Film>>() {
            @Override
            public void onResponse(Call<List<Film>> call, Response<List<Film>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onLoadFilmsSuccess(response.body());
                } else {
                    listener.onLoadFilmsError("No films available or API error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Film>> call, Throwable t) {
                listener.onLoadFilmsError("Error connecting to the API: " + t.getMessage());
            }
        });

    }


}
