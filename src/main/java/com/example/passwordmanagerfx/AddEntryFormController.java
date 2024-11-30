package com.example.passwordmanagerfx;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.util.Date;

public class AddEntryFormController {
    private AppController appController;
    @FXML
    private TextField title;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private TextField Confirm_password;
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    protected void handleSave() {
        String EntryTitle = title.getText();
        String EntryEmail = email.getText();
        String EntryPassword = password.getText();
        String EntryConfirm_password = Confirm_password.getText();

        if (!EntryPassword.equals(EntryConfirm_password)){
            showAlert("Error", "Passwords do not match");
            return;
        }
        try {
            String encryptedPassword = EncryptionUtil.encrypt(EntryPassword);
            int userId = SessionManager.getLoggedInUserId();
            PasswordEntry entry = new PasswordEntry();
            entry.setUserID(userId);
            entry.setTitle(EntryTitle);
            entry.setEmail(EntryEmail);
            entry.setPassword(encryptedPassword);
            entry.setCreatedAt(new Date());
            entry.setUpdatedAt(new Date());
            PasswordDAO.addPassword(entry);
            closeForm(); showAlert("Success", "Password entry saved successfully");
            appController.loadPasswordEntries();
        } catch (Exception e) { e.printStackTrace();
            showAlert("Error", "Failed to save the entry. Please try again."); }
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
