package com.svalero.javaFX.controller;

import com.svalero.javaFX.service.ApiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label responseLabel;

    private final ApiService apiService = new ApiService();

    @FXML
    private void fetchData() {
        apiService.getData(response -> {
            Platform.runLater(() -> updateResponseLabel(response));
        });
    }

    public void updateResponseLabel(String jsonResponse) {
        try {
            // Actualizar el label en el hilo de JavaFX
            responseLabel.setText("test");

        } catch (Exception e) {
            responseLabel.setText("Error al procesar la respuesta");
            e.printStackTrace();
        }
    }
}
