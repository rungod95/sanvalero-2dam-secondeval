package com.example.android.view;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.TextView;

import com.example.android.R;
import com.example.android.api.FilmApi;
import com.example.android.api.FilmApiInterface;
import com.example.android.domain.Film;
import com.example.android.util.BaseActivity;

import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFilmView extends BaseActivity {

    private TextView titleTextView;
    private TextView genreTextView;
    private TextView releaseDateTextView;
    private TextView durationTextView;
    private long filmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        setToolbarTitle("Film Detail");

        titleTextView = findViewById(R.id.titleTextView);
        genreTextView = findViewById(R.id.genreTextView);
        releaseDateTextView = findViewById(R.id.releaseDateTextView);
        durationTextView = findViewById(R.id.durationTextView);

        // Obtener el ID de la película desde el intent
        filmId = getIntent().getLongExtra("filmId", -1);

        if (filmId != -1) {
            fetchFilmDetails(filmId);
        } else {
            Toast.makeText(this, "Invalid film ID", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected boolean shouldHideMenu() {
        return false;
    }

    private void fetchFilmDetails(long filmId) {
        FilmApiInterface apiService = FilmApi.buildInstance();
        apiService.getFilm((int) filmId).enqueue(new Callback<Film>() {
            @Override
            public void onResponse(Call<Film> call, Response<Film> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Film film = response.body();
                    displayFilmDetails(film);
                } else {
                    Toast.makeText(DetailFilmView.this, "Error fetching film details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Film> call, Throwable t) {
                Toast.makeText(DetailFilmView.this, "API connection failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayFilmDetails(Film film) {
        titleTextView.setText(film.getTitle());
        genreTextView.setText(film.getGenre());

        // Convertir LocalDate a String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        releaseDateTextView.setText(film.getReleaseDate().format(formatter));

        durationTextView.setText(film.getDuration() + " min");
    }
}
