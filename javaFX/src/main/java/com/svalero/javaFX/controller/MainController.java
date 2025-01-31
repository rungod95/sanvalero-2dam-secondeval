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
            // JSON parse
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Extract desired field from the JSON response
            String fact = rootNode.get("fact").asText();

            // Update the response label with the field from the JSON response
            responseLabel.setText(fact);

        } catch (Exception e) {
            responseLabel.setText("Error processing response");
            e.printStackTrace();
        }
    }
}
