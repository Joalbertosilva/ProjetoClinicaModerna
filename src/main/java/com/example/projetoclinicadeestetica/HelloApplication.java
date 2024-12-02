package com.example.projetoclinicadeestetica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/projetoclinicadeestetica/tela-inicial.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 694, 495);
        stage.setTitle("Clínica de Estética");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
