package com.example.projetoclinicadeestetica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BancoDeDados {
    private static final String URL = "jdbc:postgresql://localhost:5432/ClinicaEstetica";
    private static final String USER = "postgres";
    private static final String PASSWORD = "joao";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
