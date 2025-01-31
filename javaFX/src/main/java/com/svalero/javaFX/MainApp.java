package com.svalero.javaFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/svalero/javaFX/view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
        stage.setTitle("JavaFX Async API Example");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // Inicia la aplicación JavaFX
    }
}