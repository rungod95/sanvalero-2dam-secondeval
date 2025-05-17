package com.example.android.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;
import com.example.android.contract.RegisterFilmContract;
import com.example.android.domain.Director;
import com.example.android.domain.Film;
import com.example.android.presenter.RegisterFilmPresenter;
import com.example.android.api.DirectorApi;
import com.example.android.api.DirectorApiInterface;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFilmView extends AppCompatActivity implements RegisterFilmContract.View {

    private EditText titleEt, genreEt, releaseDateEt, durationEt;
    private CheckBox viewedCb;
    private Button saveBtn;
    private MaterialAutoCompleteTextView spinnerDirectors;
    private List<Director> directorList = new ArrayList<>();

    private RegisterFilmPresenter presenter;
    private long filmId = -1;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_film);

        titleEt         = findViewById(R.id.et_film_title);
        genreEt         = findViewById(R.id.et_film_genre);
        releaseDateEt   = findViewById(R.id.et_film_release_date);
        durationEt      = findViewById(R.id.et_film_duration);
        viewedCb        = findViewById(R.id.cb_film_viewed);
        saveBtn         = findViewById(R.id.btn_save_film);
        spinnerDirectors= findViewById(R.id.spinner_directors);

        presenter = new RegisterFilmPresenter(this);

        // Carga directores y, al acabar, si es edición, carga datos de la película
        loadDirectors();

        filmId = getIntent().getLongExtra("filmId", -1);

        saveBtn.setOnClickListener(v -> {
            // Recoger y validar campos
            String title = titleEt.getText().toString().trim();
            String genre = genreEt.getText().toString().trim();
            String rd    = releaseDateEt.getText().toString().trim();
            boolean viewed = viewedCb.isChecked();
            int duration;
            try {
                duration = Integer.parseInt(durationEt.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Enter a valid duration", Toast.LENGTH_SHORT).show();
                return;
            }
            if (title.isEmpty() || genre.isEmpty() || rd.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener texto seleccionado y buscar el Director correspondiente
            String selectedName = spinnerDirectors.getText().toString();
            Director selectedDirector = null;
            for (Director d : directorList) {
                String full = d.getName() + " " + d.getLastName();
                if (full.equals(selectedName)) {
                    selectedDirector = d;
                    break;
                }
            }
            if (selectedDirector == null) {
                Toast.makeText(this, "Select a valid director", Toast.LENGTH_SHORT).show();
                return;
            }

            // Llamar al presenter según creación o edición
            if (filmId == -1) {
                presenter.saveFilm(title, genre, rd, duration, viewed, selectedDirector);
            } else {
                presenter.editFilm(filmId, title, genre, rd, duration, viewed, selectedDirector);
            }
        });
    }

    private void loadDirectors() {
        DirectorApiInterface api = DirectorApi.buildInstance();
        api.getAllDirectors().enqueue(new Callback<List<Director>>() {
            @Override
            public void onResponse(Call<List<Director>> call, Response<List<Director>> resp) {
                if (!resp.isSuccessful() || resp.body() == null) {
                    Toast.makeText(RegisterFilmView.this, "Error loading directors", Toast.LENGTH_SHORT).show();
                    return;
                }
                directorList = resp.body();

                // Preparar lista de nombres
                List<String> names = new ArrayList<>();
                for (Director d : directorList) {
                    names.add(d.getName() + " " + d.getLastName());
                }

                // Adaptador para MaterialAutoCompleteTextView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    RegisterFilmView.this,
                    android.R.layout.simple_list_item_1,
                    names
                );
                spinnerDirectors.setAdapter(adapter);

                // Si estamos editando, cargamos los datos de la película
                if (filmId != -1) {
                    setTitle("Edit Film");
                    fetchFilmDetails(filmId);
                }
            }

            @Override
            public void onFailure(Call<List<Director>> call, Throwable t) {
                Toast.makeText(RegisterFilmView.this, "Network error loading directors", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchFilmDetails(long id) {
        presenter.fetchFilmDetails(id, new RegisterFilmContract.Model.OnFetchFilmListener() {
            @Override
            public void onSuccess(Film film) {
                titleEt.setText(film.getTitle());
                genreEt.setText(film.getGenre());
                releaseDateEt.setText(film.getReleaseDate().toString());
                durationEt.setText(String.valueOf(film.getDuration()));
                viewedCb.setChecked(film.isViewed());

                // Posicionar el AutoCompleteTextView en el director actual
                if (film.getDirector() != null) {
                    String full = film.getDirector().getName()
                        + " " + film.getDirector().getLastName();
                    spinnerDirectors.setText(full, false);
                }
            }
            @Override
            public void onError(String message) {
                Toast.makeText(RegisterFilmView.this, "Error loading: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
