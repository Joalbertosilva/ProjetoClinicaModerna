/*package com.example.projetoclinicadeestetica;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenManager2 {
    // Método específico para carregar a tela de "finalizarAgendamento"
    public static void loadScreenForFinalizarAgendamento(Stage stage, String fxmlFile, Integer userId, String userName, String userEmail, String serviceName, String professionalName, String day, String hour) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenManager.class.getResource("/com/example/projetoclinicadeestetica/" + fxmlFile));
            Parent root = loader.load();

            // Passar os dados diretamente para o controlador da tela
            Object controller = loader.getController();
            if (controller instanceof FinalizarAgendaController) {
                FinalizarAgendaController finalizarAgendaController = (FinalizarAgendaController) controller;
                finalizarAgendaController.setData(userId, userName, userEmail, serviceName, professionalName, day, hour);
            }

            // Exibir a nova tela
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
