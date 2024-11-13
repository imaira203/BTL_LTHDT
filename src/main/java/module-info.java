module com.application.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires java.sql;


    opens com.application.main to javafx.fxml;
    exports com.application.main;
    exports com.application.controller;
    opens com.application.controller to javafx.fxml;
}