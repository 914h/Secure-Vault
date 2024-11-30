package com.example.passwordmanagerfx;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditEntryFormController {

    private AppController appController;

    public void setAppController(AppController appController) {
        this.appController = appController;
    }
    @FXML
    private TextField title;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;

    private PasswordEntry passwordEntry;

    public void setPasswordEntry(PasswordEntry passwordEntry) {
        this.passwordEntry = passwordEntry;
        title.setText(passwordEntry.getTitle());
        email.setText(passwordEntry.getEmail());
        try {
            String decryptedPassword = EncryptionUtil.decrypt(passwordEntry.getPassword());
            password.setText(decryptedPassword);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to decrypt the password.");
        }
    }

    @FXML
    protected void handleSave() {
        if (password.getText().isEmpty()) {
            showAlert("Error", "Password cannot be empty.");
            return;
        }
        if (password.getText().equals(confirmPassword.getText())) {
            try {
                String encryptedPassword = EncryptionUtil.encrypt(password.getText());

                passwordEntry.setTitle(title.getText());
                passwordEntry.setEmail(email.getText());
                passwordEntry.setPassword(encryptedPassword);

                PasswordDAO.updatePassword(passwordEntry);
                appController.loadPasswordEntries();
                showAlert("Success", "Password entry updated successfully.");
                closeForm();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to save the entry. Please try again.");
            }
        } else {
            showAlert("Error", "Passwords do not match.");
        }
    }

    @FXML
    protected void handleCancel() {
        System.out.println("Form canceled");
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

