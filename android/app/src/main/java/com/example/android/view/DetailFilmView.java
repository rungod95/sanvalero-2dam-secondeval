package com.example.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

    private TextView    titleTextView, genreTextView, directorTextView, releaseDateTextView, durationTextView;
    private Button editBtn, deleteBtn;
    private long        filmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        setToolbarTitle("Film Detail");

        // 1) Referencias UI
        titleTextView       = findViewById(R.id.titleTextView);
        genreTextView       = findViewById(R.id.genreTextView);
        directorTextView = findViewById(R.id.directorTextView);
        releaseDateTextView = findViewById(R.id.releaseDateTextView);
        durationTextView    = findViewById(R.id.durationTextView);
        editBtn             = findViewById(R.id.btn_edit_film);
        deleteBtn           = findViewById(R.id.btn_delete_film);

        // 2) Obtener ID y cargar detalles
        filmId = getIntent().getLongExtra("filmId", -1);
        if (filmId != -1) {
            fetchFilmDetails(filmId);
        } else {
            Toast.makeText(this, "Invalid film ID", Toast.LENGTH_SHORT).show();
        }

        // 3) Editar → RegisterFilmView en modo “edit”
        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DetailFilmView.this, RegisterFilmView.class);
            intent.putExtra("filmId", filmId);
            startActivity(intent);
            finish(); // así, al volver, listará de nuevo en onResume()
        });

        // 4) Eliminar → llamada DELETE y cierre
        deleteBtn.setOnClickListener(v -> {
            FilmApiInterface apiService = FilmApi.buildInstance();
            apiService.deleteFilm(filmId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(DetailFilmView.this,
                                "Film deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(DetailFilmView.this,
                                "Failed to delete: " + response.code(),
                                Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(DetailFilmView.this,
                            "API error: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    }
                });
        });
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

        // Mostrar director (comprueba que no sea null)
        if (film.getDirector() != null) {
            String fullName = film.getDirector().getName()
                + " " + film.getDirector().getLastName();
            directorTextView.setText("Director: " + fullName);
        } else {
            directorTextView.setText("Director: N/A");
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        releaseDateTextView.setText(film.getReleaseDate().format(fmt));
        durationTextView.setText(film.getDuration() + " min");
    }

}
