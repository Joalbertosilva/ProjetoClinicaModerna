/*package com.example.projetoclinicadeestetica;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class ScreenManager {

    // Carregar a tela e passar dados para o controlador
    public static void loadScreen(Stage stage, String fxmlFile, Map<String, Integer> data) {
        try {
            // Ajustar o caminho do FXML para garantir que está sendo encontrado corretamente
            FXMLLoader loader = new FXMLLoader(ScreenManager.class.getResource("/com/example/projetoclinicadeestetica/" + fxmlFile));
            Parent root = loader.load();

            // Passar dados ao próximo controlador
            Object controller = loader.getController();
            if (controller instanceof ProfissionalController) {
                ((ProfissionalController) controller).setData(data);
            }

            // Exibir a tela (gerenciar a navegação)
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
