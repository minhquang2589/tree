module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires java.desktop;
    requires java.prefs;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    opens com.example.demo.controller.auth to javafx.fxml;
    opens com.example.demo.config.button to javafx.fxml;
    opens com.example.demo.config.loading to javafx.fxml;
    opens com.example.demo.config;
    exports com.example.demo.config;
    exports com.example.demo.controller;
    opens com.example.demo.controller to javafx.fxml;
    exports com.example.demo.controller.admin;
    opens com.example.demo.controller.admin to javafx.fxml;
    opens com.example.demo.model to javafx.base;
}
