package com.example.projetoclinicadeestetica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ServicesController {
    private int userId;
    private String userName;

    public void setUserData(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    @FXML
    private GridPane serviceButtonsContainer; // Container com os botões de serviços

    @FXML
    private Button nextButton; // Botão "Próximo"

    private Map<Integer, Button> serviceButtonsMap = new HashMap<>();
    private Integer selectedServiceId;

    @FXML
    public void initialize() {
        try {
            loadServicesFromDatabase(); // Carrega os serviços do banco
            nextButton.setOnAction(event -> goToProfissionalsScreen());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Carregar os serviços do banco de dados
    private void loadServicesFromDatabase() throws SQLException {
        Connection connection = BancoDeDados.getConnection();
        String query = "SELECT * FROM servicos";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        int row = 0;
        int col = 0;

        while (resultSet.next()) {
            int serviceId = resultSet.getInt("id");
            String serviceName = resultSet.getString("nome");
            double servicePrice = resultSet.getDouble("valor");

            Button serviceButton = new Button(serviceName + " R$" + servicePrice);
            serviceButton.setId(String.valueOf(serviceId)); // Define o ID do serviço no botão
            serviceButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> selectService(serviceButton, serviceId));

            // Adiciona os botões no GridPane
            serviceButtonsContainer.add(serviceButton, col, row);

            // Mapeia o serviço e o botão
            serviceButtonsMap.put(serviceId, serviceButton);

            // Atualiza a posição das células no GridPane
            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }

        connection.close();
    }

    // Lógica para seleção de serviço
    private void selectService(Button serviceButton, int serviceId) {
        // Reseta bordas de todos os botões antes de aplicar a nova seleção
        for (Button button : serviceButtonsMap.values()) {
            button.setStyle(""); // Reseta o estilo
        }

        // Define borda verde no botão selecionado
        serviceButton.setStyle("-fx-border-color: green; -fx-border-width: 2px;");

        // Armazena o ID do serviço selecionado
        selectedServiceId = serviceId;
    }

    // Lógica para navegar para a próxima tela
    private void goToProfissionalsScreen() {
        if (selectedServiceId != null) {
            Map<String, Integer> userSelections = new HashMap<>();
            userSelections.put("serviceId", selectedServiceId);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("escolherProfissional.fxml"));
                Parent root = loader.load();

                ProfissionalController controller = loader.getController();
                controller.setUserData(userId, userName, userSelections);

                Stage stage = (Stage) nextButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Por favor, selecione um serviço antes de continuar.");
        }
    }
}
