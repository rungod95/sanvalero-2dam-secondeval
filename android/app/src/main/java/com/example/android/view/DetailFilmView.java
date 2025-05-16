package com.example.android.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import com.example.android.R;
import com.example.android.api.AuthApi;
import com.example.android.api.AuthApiInterface;
import com.example.android.api.FilmApi;
import com.example.android.api.FilmApiInterface;
import com.example.android.domain.Film;
import com.example.android.util.BaseActivity;

import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFilmView extends BaseActivity {

    private TextView    titleTextView, genreTextView, directorTextView,
        releaseDateTextView, durationTextView;
    private Button      editBtn, deleteBtn;
    private long        filmId;

    // Campos nuevos
    private String              authToken;
    private FilmApiInterface    filmApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        setToolbarTitle("Film Detail");

        // 1) Referencias UI
        titleTextView = findViewById(R.id.titleTextView);
        genreTextView = findViewById(R.id.genreTextView);
        directorTextView = findViewById(R.id.directorTextView);
        releaseDateTextView = findViewById(R.id.releaseDateTextView);
        durationTextView = findViewById(R.id.durationTextView);
        editBtn = findViewById(R.id.btn_edit_film);
        deleteBtn = findViewById(R.id.btn_delete_film);

        // 2) Obtener filmId
        filmId = getIntent().getLongExtra("filmId", -1);

        // 3) Autenticación automática (POST form-urlencoded)
        AuthApiInterface authApi = AuthApi.buildInstance();
        authApi.login("user", "password").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    authToken = resp.body();
                    // Construyo filmApi con el token
                    filmApi = FilmApi.buildInstance(authToken);
                    // Una vez tengo filmApi, cargo detalles
                    if (filmId != -1) {
                        fetchFilmDetails(filmId);
                    }
                } else {
                    Toast.makeText(DetailFilmView.this,
                        "Auth failed: " + resp.code(),
                        Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(DetailFilmView.this,
                    "Network auth error",
                    Toast.LENGTH_SHORT).show();
            }
        });

        // 4) Editar → RegisterFilmView
        editBtn.setOnClickListener(v -> {
            Intent i = new Intent(DetailFilmView.this, RegisterFilmView.class);
            i.putExtra("filmId", filmId);
            startActivity(i);
            finish();
        });

        // 5) Eliminar → DELETE con filmApi
        deleteBtn.setOnClickListener(v -> {
            if (filmApi == null) {
                Toast.makeText(this, "Esperando autenticación…", Toast.LENGTH_SHORT).show();
                return;
            }
            new AlertDialog.Builder(this)
                .setTitle("Eliminar película")
                .setMessage("¿Estás seguro de que quieres borrar esta película?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // llamada al DELETE
                    filmApi.deleteFilm(filmId).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> c, Response<Void> r) {
                            if (r.isSuccessful()) {
                                Toast.makeText(DetailFilmView.this,
                                    "Película eliminada", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(DetailFilmView.this,
                                    "Error al eliminar: " + r.code(),
                                    Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> c, Throwable t) {
                            Toast.makeText(DetailFilmView.this,
                                "Error de red al eliminar", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
        });
    }


        @Override
    protected boolean shouldHideMenu() {
        return false;
    }

    private void fetchFilmDetails(long id) {
        if (filmApi == null) return;
        filmApi.getFilm(id).enqueue(new Callback<Film>() {
            @Override
            public void onResponse(Call<Film> call, Response<Film> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayFilmDetails(response.body());
                } else {
                    Toast.makeText(DetailFilmView.this,
                        "Error fetching film details",
                        Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Film> call, Throwable t) {
                Toast.makeText(DetailFilmView.this,
                    "API connection failed",
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayFilmDetails(Film film) {
        titleTextView.setText(film.getTitle());
        genreTextView.setText(film.getGenre());

        if (film.getDirector() != null) {
            directorTextView.setText(
                film.getDirector().getName()
                    + " " + film.getDirector().getLastName()
            );
        } else {
            directorTextView.setText("Director: N/A");
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        releaseDateTextView.setText(film.getReleaseDate().format(fmt));
        durationTextView.setText(film.getDuration() + " min");
    }
}
