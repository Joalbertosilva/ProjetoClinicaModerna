package com.example.projetoclinicadeestetica;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CadastroController {
    public TextField nomeTextField;
    public TextField cpfTextField;
    public TextField senhaTextField;
    public TextField emailTextField;
    public TextField idadeTextField;
    public TextField enderecoTextField;
    public Button cadastroBotao;

    public void onCadastrarButtonClick(ActionEvent actionEvent) {
        String nome = nomeTextField.getText();
        String cpf = cpfTextField.getText();
        String senha = senhaTextField.getText();
        String email = emailTextField.getText();
        String idadeStr = idadeTextField.getText();
        String endereco = enderecoTextField.getText();

        // Validação simples
        if (nome.isEmpty() || cpf.isEmpty() || senha.isEmpty() || email.isEmpty()) {
            showAlert("Erro", "Por favor, preencha todos os campos obrigatórios!");
            return;
        }

        try {
            int idade = idadeStr.isEmpty() ? 0 : Integer.parseInt(idadeStr); // Default para 0 se vazio
            salvarUsuario(nome, cpf, senha, email, idade, endereco);
            showAlert("Sucesso", "Usuário cadastrado com sucesso!");
            limparCampos();
        } catch (NumberFormatException e) {
            showAlert("Erro", "Idade deve ser um número válido!");
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao salvar o usuário no banco: " + e.getMessage());
        }
    }

    private void salvarUsuario(String nome, String cpf, String senha, String email, int idade, String endereco) throws SQLException {
        String sql = "INSERT INTO Usuario (nome, cpf, senha, email, idade, endereco) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = BancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            stmt.setString(3, senha);
            stmt.setString(4, email);
            stmt.setInt(5, idade);
            stmt.setString(6, endereco);

            stmt.executeUpdate();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void limparCampos() {
        nomeTextField.clear();
        cpfTextField.clear();
        senhaTextField.clear();
        emailTextField.clear();
        idadeTextField.clear();
        enderecoTextField.clear();
    }

    public void onEntrarButtonClick(ActionEvent actionEvent) {
        try {
            // Carregar a tela de cadastro
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tela-inicial.fxml"));
            Parent cadastroRoot = fxmlLoader.load();

            // Obter o estágio atual
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();

            // Definir a nova cena
            stage.setScene(new Scene(cadastroRoot));
            stage.setTitle("Entrada de Usuário");
        } catch (Exception e) {
            System.out.println("Erro ao redirecionar para a tela de cadastro: " + e.getMessage());
        }
    }
}
