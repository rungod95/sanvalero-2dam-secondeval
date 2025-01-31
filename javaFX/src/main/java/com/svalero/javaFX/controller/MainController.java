package com.svalero.javaFX.controller;

import com.svalero.javaFX.service.ApiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML
    private ListView<String> filmListView;  // ListView para mostrar los títulos de las películas

    private final ApiService apiService = new ApiService();
    private final ObservableList<String> filmTitles = FXCollections.observableArrayList(); // Lista observable

    @FXML
    public void initialize() {
        filmListView.setItems(filmTitles); // Conectar la lista con el ListView
    }

    @FXML
    private void fetchData() {
        apiService.getData(response -> {
            Platform.runLater(() -> updateFilmList(response));
        });
    }

    public void updateFilmList(String jsonResponse) {
        try {
            // JSON parse
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            if (rootNode.isArray()) {
                List<String> films = new ArrayList<>();
                for (JsonNode filmNode : rootNode) {
                    String title = filmNode.get("title").asText();
                    String genre = filmNode.get("genre").asText();


                    JsonNode directorNode = filmNode.get("director");
                    String directorName = "Unknown Director";

                    if (directorNode != null) {
                        String firstName = directorNode.get("name").asText();
                        String lastName = directorNode.get("lastName").asText();
                        directorName = firstName + " " + lastName;
                    }

                    // Concatenar la información de la película
                    String filmString = title + " | " + genre + " | Dir: " + directorName;
                    films.add(filmString);
                }

                // Actualizar la lista observable
                filmTitles.setAll(films);
            } else {
                filmTitles.setAll("Error: Unexpected JSON format");
            }

        } catch (Exception e) {
            filmTitles.setAll("Error processing response");
            e.printStackTrace();
        }
    }
}
