package com.svalero.javaFX.controller;

import com.svalero.javaFX.service.ApiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML
    private VBox filmContainer;

    private final ApiService apiService = new ApiService();
    private final ObservableList<String> filmTitles = FXCollections.observableArrayList(); // Lista observable



    @FXML
    private void fetchData() {
        apiService.getData(response -> {
            Platform.runLater(() -> updateFilmContainer(response));
        });
    }

    private void updateFilmContainer(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            filmContainer.getChildren().clear(); // Limpiar antes de añadir nuevos datos

            if (rootNode.isArray()) {
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


                    Label filmLabel = new Label("🎬 " + title + " (" + genre + ") - Directed by: " + directorName);
                    filmLabel.getStyleClass().add("film-label");
                    filmContainer.getChildren().add(filmLabel);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }}
