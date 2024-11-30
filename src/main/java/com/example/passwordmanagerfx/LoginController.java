package com.example.passwordmanagerfx;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField EmailField;
    @FXML
    private TextField passwordField;
    @FXML
    protected void handleLogin() {
        String EmailField = this.EmailField.getText();
        String PassEntry = passwordField.getText();
        if (EmailField.isEmpty() || PassEntry.isEmpty()) {
            showAlert("Error", "EmailField and Password cannot be empty.");
            return;
        }
        try {
            boolean isValidUser = UserDAO.ValidatedUser(EmailField, PassEntry);
            if (isValidUser) {
                int userId = UserDAO.getUserIdByEmail(EmailField);
                SessionManager.setLoggedInUserId(userId);
                showAlert("Success", "Login successful!");
                Stage stage = (Stage) this.EmailField.getScene().getWindow();
                stage.close();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
                    Stage mainStage = new Stage();
                    Scene scene = new Scene(loader.load());
                    mainStage.setTitle("Password App Manager");
                    mainStage.getIcons().add(new Image(PasswordApplication.class.getResourceAsStream("/img/logo.png")));
                    mainStage.setScene(scene);
                    mainStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to load the main view. Please try again. Error: " + e.getMessage());
                }
            } else {
                showAlert("Error", "Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while trying to log in. Please try again later.");
        }
    }

    @FXML
    private TextField NameFieldSg;
    @FXML
    private TextField emailField;
    @FXML
    private TextField signupPasswordField;

    @FXML
    private void handleSignUp() throws SQLException {
        String NameEntry = NameFieldSg.getText();
        String EmailEntry = emailField.getText();
        String PassEntry = signupPasswordField.getText();

        if (NameEntry.isEmpty() || EmailEntry.isEmpty() || PassEntry.isEmpty()) {
            showAlert("Error", "Name, Email, and Password cannot be empty.");
            return;
        }

        boolean isInserted = UserDAO.ValidatedInsertUser(NameEntry,EmailEntry, PassEntry);

        if (isInserted) {
            showAlert("Success", "Sign UP successful!");
            Stage stage = (Stage) this.EmailField.getScene().getWindow();
            stage.close();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
                Stage mainStage = new Stage();
                Scene scene = new Scene(loader.load());
                mainStage.setTitle("Password Manager App");
                mainStage.getIcons().add(new Image(PasswordApplication.class.getResourceAsStream("/img/logo.png")));

                mainStage.setScene(scene);
                mainStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load the main view. Please try again. Error: " + e.getMessage());
            }
        } else {
            showAlert("Error", "Invalid username or password.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
