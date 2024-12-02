package com.example.projetoclinicadeestetica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;  // Importando Image para carregar a imagem
import javafx.scene.image.ImageView;  // Importando ImageView para exibir a imagem
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MarcarController {

    private Integer selectedDayId; // ID do dia selecionado
    private Integer selectedHourId; // ID do horário selecionado
    private Integer professionalId; // ID do profissional
    private Integer serviceId; // ID do serviço
    private Integer userId; // ID do usuário
    private String userName;

    @FXML
    private Label userNameLabel;

    public void setUserData(int userId, String userName, Map<String, Integer> data) {
        this.userId = userId;
        this.userName = userName;
        this.serviceId = data.get("serviceId");
        this.professionalId = data.get("professionalId");
        System.out.println("Usuário: " + userName + ", Serviço ID: " + serviceId + ", Profissional ID: " + professionalId);
    }


    // Carregar horários disponíveis do banco de dados
    @FXML
    public void loadAvailableHours() {
        try (Connection connection = BancoDeDados.getConnection()) {
            String query = """
                    SELECT h.id, h.horario, d.nome AS dia, 
                           CASE WHEN a.id IS NULL THEN 'DISPONÍVEL' ELSE 'INDISPONÍVEL' END AS status
                    FROM horarios h
                    CROSS JOIN dias_semana d
                    LEFT JOIN agendamentos a 
                    ON h.id = a.id_horario AND d.id = a.id_dia AND a.id_profissional = ?
                    """;

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, professionalId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int hourId = rs.getInt("id");
                String hourText = rs.getString("horario");
                String dayText = rs.getString("dia");
                String status = rs.getString("status");

                System.out.printf("Horário: %s (%s) - %s\n", hourText, dayText, status);
                // Aqui, você pode adicionar os horários dinamicamente ao layout
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Seleção de um dia da semana
    @FXML
    public void selectDay(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        // Verifica se o ID do botão está no formato correto
        try {
            selectedDayId = Integer.parseInt(clickedButton.getId().replace("day_", ""));
            System.out.println("Dia selecionado: " + clickedButton.getText());
        } catch (NumberFormatException e) {
            System.out.println("Erro ao selecionar o dia: ID do botão inválido.");
            return;
        }

        // Visualmente destacar o botão selecionado (opcional)
        clickedButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
    }

    // Seleção de um horário
    @FXML
    public void selectHour(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        // Atribuir o ID do horário à variável selectedHourId
        selectedHourId = Integer.parseInt(clickedButton.getId().replace("hour_", ""));  // Supondo que o ID do botão seja "hour_X"

        // Criar a nova imagem confirmando o agendamento
        ImageView newImage = new ImageView(new Image(getClass().getResource("/com/example/projetoclinicadeestetica/190411.png").toExternalForm()));
        newImage.setFitHeight(20);  // Ajuste do tamanho da imagem
        newImage.setFitWidth(20);   // Ajuste da largura da imagem

        // Definir a nova imagem no botão
        clickedButton.setGraphic(newImage);
        clickedButton.setContentDisplay(ContentDisplay.CENTER.RIGHT);  // Alinha a imagem à direita do botão

        System.out.println("Horário selecionado: " + clickedButton.getText());
        System.out.println("Imagem de confirmação de agendamento definida com sucesso.");
    }

    // Finalizar o agendamento
    @FXML
    public void finalizeBooking(ActionEvent event) {
        if (selectedDayId == null) {
            System.out.println("Erro: Nenhum dia foi selecionado!");
            return;
        }

        if (selectedHourId == null) {
            System.out.println("Erro: Nenhum horário foi selecionado!");
            return;
        }

        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("userId", userId);
        bookingData.put("userName", userName);
        bookingData.put("serviceId", serviceId);
        bookingData.put("professionalId", professionalId);
        bookingData.put("day", selectedDayId);
        bookingData.put("hour", selectedHourId);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("finalizarAgenda.fxml"));
            Parent root = loader.load();

            FinalizarAgendaController controller = loader.getController();
            controller.setUserData(bookingData);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para obter o nome do serviço (exemplo)
    private String getServiceName(Integer serviceId) {
        switch (serviceId) {
            case 1: return "Corte";
            case 2: return "Corte + sobrancelha";
            case 3: return "Corte + barba";
            default: return "Serviço desconhecido";
        }
    }

    // Método para obter o email do usuário (exemplo)
    private String getUserEmail(Integer userId) {
        try (Connection connection = BancoDeDados.getConnection()) {
            String query = "SELECT email FROM usuarios WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Email não encontrado";
    }

    // Método para obter o nome do profissional (exemplo)
    private String getProfessionalName(Integer professionalId) {
        try (Connection connection = BancoDeDados.getConnection()) {
            String query = "SELECT nome FROM profissionais WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, professionalId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("nome");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Profissional desconhecido";
    }

    // Método para obter o nome do dia (exemplo)
    private String getDayName(Integer dayId) {
        switch (dayId) {
            case 1: return "Segunda-feira";
            case 2: return "Terça-feira";
            case 3: return "Quarta-feira";
            case 4: return "Quinta-feira";
            case 5: return "Sexta-feira";
            case 6: return "Sábado";
            default: return "Dia desconhecido";
        }
    }

    // Método para obter o intervalo de horários (exemplo)
    private String getHourRange(Integer hourId) {
        switch (hourId) {
            case 1: return "08:00 às 08:30";
            case 2: return "08:30 às 09:00";
            case 3: return "09:00 às 09:30";
            case 4: return "09:30 às 10:00";
            // Adicione os outros intervalos aqui
            default: return "Horário desconhecido";
        }
    }

    // Obter o nome do usuário (consulta no banco de dados)
    private String getUserName(Integer userId) {
        try (Connection connection = BancoDeDados.getConnection()) {
            String query = "SELECT nome FROM usuarios WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("nome");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Usuário desconhecido";
    }

    /*public void setData(Map<String, Integer> data) {
        this.serviceId = data.get("serviceId");
        this.professionalId = data.get("professionalId");
        System.out.println("Serviço ID: " + serviceId);
        System.out.println("Profissional ID: " + professionalId);
    }*/

}
