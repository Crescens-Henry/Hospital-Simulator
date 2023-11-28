module com.example.hospital_simulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.hospital_simulator to javafx.fxml;
    exports com.example.hospital_simulator;
    exports com.example.hospital_simulator.controller;
    opens com.example.hospital_simulator.controller to javafx.fxml;
}