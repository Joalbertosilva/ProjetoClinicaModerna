package com.example.projetoclinicadeestetica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfissionalController {

    @FXML
    private Label serviceLabel;
    @FXML
    private Button chooseButton1;
    @FXML
    private Button chooseButton2;

    private Integer userId;
    private String userName;
    private Integer serviceId;
    private Integer selectedProfessionalId;

    private Button lastSelectedButton;

    public void setUserData(int userId, String userName, Map<String, Integer> data) {
        this.userId = userId;
        this.userName = userName;
        this.serviceId = data.get("serviceId");
        serviceLabel.setText("Serviço: " + getServiceName(serviceId));
    }

    // Lógica para escolher o profissional João Alberto (ID 1)
    @FXML
    public void chooseProfessional1() {
        selectedProfessionalId = 1;
        highlightSelectedButton(chooseButton1);
        System.out.println("Profissional selecionado: João Alberto");
    }

    @FXML
    public void chooseProfessional2() {
        selectedProfessionalId = 2;
        highlightSelectedButton(chooseButton2);
        System.out.println("Profissional selecionado: Rafael Silva");
    }

    // Destacar o botão selecionado
    private void highlightSelectedButton(Button selectedButton) {
        if (lastSelectedButton != null) {
            lastSelectedButton.setStyle("-fx-background-color: #C4AB6F;");
        }

        selectedButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        lastSelectedButton = selectedButton;
    }

    // Ação para finalizar o agendamento e navegar para a tela de marcar serviço
    @FXML
    private void finalizeBooking(ActionEvent event) {
        if (selectedProfessionalId == null) {
            System.out.println("Nenhum profissional foi selecionado!");
            return;
        }

        Map<String, Integer> userSelections = new HashMap<>();
        userSelections.put("serviceId", serviceId);
        userSelections.put("professionalId", selectedProfessionalId);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("marcarServiço.fxml"));
            Parent root = loader.load();

            MarcarController controller = loader.getController();
            controller.setUserData(userId, userName, userSelections);

            Stage stage = (Stage) chooseButton1.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getServiceName(Integer serviceId) {
        switch (serviceId) {
            case 1: return "Corte";
            case 2: return "Corte + sobrancelha";
            case 3: return "Corte + barba";
            default: return "Serviço desconhecido";
        }
    }
}

