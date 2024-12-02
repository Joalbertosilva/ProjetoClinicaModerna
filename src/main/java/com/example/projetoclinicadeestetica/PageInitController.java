package com.example.projetoclinicadeestetica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PageInitController {

    private Stage stage;
    private Scene scene;
    private int userId;
    private String userName;

    public void setUserData(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    // Botão "Serviços"
    @FXML
    private void irParaServicos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("servicos.fxml"));
            Parent root = loader.load();

            ServicesController controller = loader.getController();
            controller.setUserData(userId, userName); // Passar os dados para a próxima tela

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
