package com.example.projetoclinicadeestetica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HelloController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField senhaField;

    @FXML
    private Label feedbackLabel;

    @FXML
    protected void onEntrarButtonClick() {
        String email = emailField.getText().trim();
        String senha = senhaField.getText().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            feedbackLabel.setText("Por favor, preencha todos os campos!");
            feedbackLabel.setTextFill(Color.RED);
            feedbackLabel.setVisible(true);
            return;
        }

        try (Connection connection = BancoDeDados.getConnection()) {
            String query = "SELECT id, nome FROM Usuario WHERE email = ? AND senha = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, senha);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String userName = resultSet.getString("nome");

                // Passar userId e userName para a próxima tela
                FXMLLoader loader = new FXMLLoader(getClass().getResource("pagina-inicial.fxml"));
                Parent root = loader.load();

                PageInitController controller = loader.getController();
                controller.setUserData(userId, userName);

                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                feedbackLabel.setText("Email ou senha incorretos!");
                feedbackLabel.setTextFill(Color.RED);
                feedbackLabel.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void trocarCena(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erro ao carregar a cena: " + e.getMessage());
        }
    }

    @FXML
    protected void onCadastrarButtonClick(ActionEvent actionEvent) {
        try {
            // Carregar a tela de cadastro
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Cadastro.fxml"));
            Parent cadastroRoot = fxmlLoader.load();

            // Obter o estágio atual
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();

            // Definir a nova cena
            stage.setScene(new Scene(cadastroRoot));
            stage.setTitle("Cadastro de Usuário");
            stage.show();
        } catch (IOException e) {
            System.out.println("Erro ao redirecionar para a tela de cadastro: " + e.getMessage());
        }
    }

    @FXML
    protected void onVoltarButtonClick() {
        try {
            // Carregar a tela inicial (login)
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tela-inicial.fxml"));
            Parent loginRoot = fxmlLoader.load();

            // Trocar a cena atual para a tela inicial
            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene scene = new Scene(loginRoot);
            stage.setScene(scene);
            stage.setTitle("Tela Inicial");
            stage.show();
        } catch (IOException e) {
            System.out.println("Erro ao voltar para a tela de login: " + e.getMessage());
        }
    }
}
