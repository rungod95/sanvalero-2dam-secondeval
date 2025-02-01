package com.example.android.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.adapter.FilmAdapter;
import com.example.android.contract.FilmListContract;
import com.example.android.domain.Film;
import com.example.android.presenter.FilmListPresenter;
import com.example.android.util.BaseActivity;

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

        // Configura la Toolbar personalizada
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new FilmListPresenter(this);
        filmList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.filmRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filmAdapter = new FilmAdapter(filmList);
        recyclerView.setAdapter(filmAdapter);
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




//     @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.addFilm) {
//            // Acción para registrar un nuevo atleta
//            Intent intent = new Intent(this, RegisterFilmView.class);
//            startActivity(intent);
//        }
//        return true;
//    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
