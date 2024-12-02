package com.example.projetoclinicadeestetica;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class FinalizarAgendaController {

    @FXML
    private Label userNameLabel;
    @FXML
    private Label serviceLabel;
    @FXML
    private Label professionalLabel;
    @FXML
    private Label dayLabel;
    @FXML
    private Label hourLabel;

    private Integer userId;
    private String userName;
    private String userEmail;
    private String serviceName;
    private String professionalName;
    private String day;
    private String hour;
    private String selectedPaymentMethod; // Armazenar o método de pagamento escolhido

    // Método para configurar os dados vindos da tela anterior
    public void setUserData(Map<String, Object> data) {
        Integer userId = (Integer) data.get("userId");
        String userName = (String) data.get("userName");
        Integer serviceId = (Integer) data.get("serviceId");
        Integer professionalId = (Integer) data.get("professionalId");
        Integer day = (Integer) data.get("day");
        Integer hour = (Integer) data.get("hour");

        userNameLabel.setText(userName);
        serviceLabel.setText("Serviço ID: " + serviceId);
        professionalLabel.setText("Profissional ID: " + professionalId);
        dayLabel.setText("Dia: " + day);
        hourLabel.setText("Horário: " + hour);

        System.out.println("Dados recebidos no controlador final:");
        System.out.println("Usuário: " + userName + ", Serviço ID: " + serviceId + ", Profissional ID: " + professionalId + ", Dia: " + day + ", Horário: " + hour);
    }

    // Selecionar método de pagamento
    @FXML
    public void selectPaymentMethod(MouseEvent event) {
        Button clickedButton = (Button) event.getSource();
        selectedPaymentMethod = clickedButton.getText(); // Captura o texto do botão como método de pagamento
        System.out.println("Método de pagamento selecionado: " + selectedPaymentMethod);
    }

    // Finalizar o agendamento e salvar no banco
    @FXML
    public void finalizeAgendamento() {
        if (selectedPaymentMethod == null) {
            System.out.println("Por favor, selecione um método de pagamento!");
            return;
        }

        try (Connection connection = BancoDeDados.getConnection()) {
            String query = """
                UPDATE agendamentos
                SET metodo_pagamento = ?
                WHERE id_usuario = ? AND id_dia = ? AND id_horario = ? AND id_servico = ?
            """;

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, selectedPaymentMethod);
            stmt.setInt(2, userId);
            stmt.setString(3, day); // Supondo que o dia é armazenado como texto no banco
            stmt.setString(4, hour); // Supondo que o horário é armazenado como texto
            stmt.setString(5, serviceName); // Supondo que o serviço é armazenado como texto
            stmt.executeUpdate();

            System.out.println("Agendamento finalizado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Encerrar a aplicação ou voltar à tela inicial
        Stage stage = (Stage) userNameLabel.getScene().getWindow();
        stage.close();
        System.out.println("Tela de finalização encerrada.");
    }

    
}
