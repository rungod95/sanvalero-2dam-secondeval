package com.example.android.view;

import android.content.Intent;                     // ← importa Intent
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.adapter.FilmAdapter;
import com.example.android.contract.FilmListContract;
import com.example.android.presenter.FilmListPresenter;
import com.example.android.domain.Film;
import com.google.android.material.floatingactionbutton.FloatingActionButton; // ← importa el FAB

import java.util.ArrayList;
import java.util.List;

public class FilmListView extends AppCompatActivity implements FilmListContract.View {

    private FilmListContract.Presenter presenter;
    private FilmAdapter filmAdapter;
    private ArrayList<Film> filmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new FilmListPresenter(this);
        filmList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.filmRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        filmAdapter = new FilmAdapter(filmList);
        recyclerView.setAdapter(filmAdapter);

        // ——— AQUÍ agregas el listener al FAB ———
        FloatingActionButton fab = findViewById(R.id.addFilm);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(FilmListView.this, RegisterFilmView.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        filmList.clear();
        presenter.loadFilms();
    }

    @Override
    public void listFilms(List<Film> filmList) {
        this.filmList.addAll(filmList);
        filmAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
