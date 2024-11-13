package com.application.controller;

import com.application.database.SupaBaseConnection;
import com.application.services.Instance;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button loginBtn;

    @FXML
    private TextField passwordInput;

    @FXML
    private TextField usernameInput;

    @FXML
    private void onMouseEntered() {
        loginBtn.setStyle("-fx-font-weight: 700; -fx-background-radius: 10px; -fx-background-color: #07a386; -fx-border-color: white; -fx-border-radius: 10px; -fx-cursor: hand;");
        loginBtn.setScaleX(1.1); // Phóng to nút 10%
        loginBtn.setScaleY(1.1);
    }

    @FXML
    private void onMouseExited() {
        loginBtn.setStyle("-fx-font-weight: 700; -fx-background-radius: 10px; -fx-background-color: #07a386; -fx-border-color: white; -fx-border-radius: 10px; -fx-cursor: hand; -fx-font-size: 18px");
        loginBtn.setScaleX(1.0); // Trả về kích thước ban đầu
        loginBtn.setScaleY(1.0);
    }

    @FXML
    void login(MouseEvent event) {
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        if(SupaBaseConnection.Login(username, password)){
            changeToMainScene();
        }
        else {
            Instance.showAlert(Alert.AlertType.ERROR, "Login Failed", "Login failed, check the username and password");
        }
    }

    private void changeToMainScene() {
        try {
            // Chuyển sang mainScene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/application/main/mainScene.fxml"));
            Scene mainScene = new Scene(loader.load());
            Stage stage = (Stage) loginBtn.getScene().getWindow();

            // Load scene mới
            stage.setScene(mainScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load main scene");
        }
    }
}
