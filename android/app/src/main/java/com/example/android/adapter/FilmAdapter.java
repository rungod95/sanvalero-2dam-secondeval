package com.example.android.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.domain.Film;

import com.example.android.view.DetailFilmView;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmHolder> {
    private final List<Film> filmList;

    public FilmAdapter(List<Film> filmList) {
        this.filmList = filmList;
    }

    @NonNull
    @Override
    public FilmAdapter.FilmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.film_item, parent, false);
        return new FilmHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmAdapter.FilmHolder holder, int position) {
        Film film = filmList.get(position);

        holder.title.setText(film.getTitle());
        holder.genre.setText("Genre: " + film.getGenre());

        if (film.getReleaseDate() != null) {
            holder.releaseDate.setText("Release Date: " + film.getReleaseDate());
        } else {
            holder.releaseDate.setText("Release Date: Not available");
        }
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    public class FilmHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView genre;
        private TextView releaseDate;

        public FilmHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_film_title);
            genre = itemView.findViewById(R.id.tv_film_genre);
            releaseDate = itemView.findViewById(R.id.tv_releaseDate);

            itemView.setOnClickListener(view -> {
                long filmId = filmList.get(getAdapterPosition()).getId();

                Intent intent = new Intent(itemView.getContext(), DetailFilmView.class);
                intent.putExtra("filmId", filmId);
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
