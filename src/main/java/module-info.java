module com.example.projetoclinicadeestetica {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens com.example.projetoclinicadeestetica to javafx.fxml;
    exports com.example.projetoclinicadeestetica;
}
