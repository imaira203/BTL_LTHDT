package com.application.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load giao diện từ fxml
        Parent root = FXMLLoader.load(getClass().getResource("loginScene.fxml"));
        // Đặt title
        primaryStage.setTitle("Quản Lí Sinh Viên");

        // Set scene
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
