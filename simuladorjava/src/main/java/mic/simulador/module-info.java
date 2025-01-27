module mic.simulador {
    requires javafx.controls;
    requires javafx.fxml;

    opens mic.simulador to javafx.fxml;
    exports mic.simulador;
}
