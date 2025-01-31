package com.svalero.javaFX.service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ApiService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String API_URL = "https://catfact.ninja/fact";

    public void getData(Consumer<String> callback) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .GET()
                .build();

        CompletableFuture.supplyAsync(() -> {
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                return response.body();
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }).thenAccept(callback);
    }
}
