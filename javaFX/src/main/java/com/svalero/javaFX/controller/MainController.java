package com.svalero.javaFX.controller;

import com.svalero.javaFX.model.Director;
import com.svalero.javaFX.model.Film;
import com.svalero.javaFX.retrofit.ApiClient;
import com.svalero.javaFX.retrofit.ApiService;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class MainController implements Initializable {

    @FXML
    private ListView<String> filmListView;

    @FXML
    private ListView<String> directorListView;

    private final ObservableList<String> filmsObservableList = FXCollections.observableArrayList();
    private final ObservableList<String> directorsObservableList = FXCollections.observableArrayList();

    private final ApiService apiService = ApiClient.getClient().create(ApiService.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filmListView.setItems(filmsObservableList);
        directorListView.setItems(directorsObservableList);
    }

    @FXML
    private void fetchData() {
        fetchFilms();
        fetchDirectors();
    }

    private void fetchFilms() {
        apiService.getAllFilms()
                .subscribeOn(Schedulers.io())
                .flatMapIterable(films -> films)
                .concatMap(film -> io.reactivex.rxjava3.core.Observable.just(film).delay(100, TimeUnit.MILLISECONDS))
                .observeOn(Schedulers.single())
                .subscribe(
                        film -> Platform.runLater(() -> {
                            String title = film.getTitle();
                            String genre = film.getGenre();
                            String director = film.getDirector() != null
                                    ? film.getDirector().getName() + " " + film.getDirector().getLastName()
                                    : "Unknown";
                            filmsObservableList.add("🎬 " + title + " (" + genre + ") - Dir: " + director);
                        }),
                        error -> Platform.runLater(() -> filmsObservableList.add("❌ Error: " + error.getMessage())),
                        () -> System.out.println("✅ Películas cargadas")
                );
    }

    private void fetchDirectors() {
        apiService.getAllDirectors()
                .subscribeOn(Schedulers.io())
                .flatMapIterable(directors -> directors)
                .concatMap(director -> io.reactivex.rxjava3.core.Observable.just(director).delay(100, TimeUnit.MILLISECONDS))
                .observeOn(Schedulers.single())
                .subscribe(
                        director -> Platform.runLater(() -> {
                            String name = director.getName() + " " + director.getLastName();
                            directorsObservableList.add("🎥 " + name + " (" + director.getNationality() + ")");
                        }),
                        error -> Platform.runLater(() -> directorsObservableList.add("❌ Error: " + error.getMessage())),
                        () -> System.out.println("✅ Directores cargados")
                );
    }
}
