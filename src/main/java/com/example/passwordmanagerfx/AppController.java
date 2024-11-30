package com.example.passwordmanagerfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class AppController {

    @FXML
    private Label welcomeText;
    @FXML
    protected void someMethod() {
        handleOpenCreateForm();
    }
    @FXML
    protected void handleOpenCreateForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_entry_form.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();

            AddEntryFormController addEntryFormController = loader.getController();
            addEntryFormController.setAppController(this);
            stage.setTitle("Password Manager App | Add New Entry");
            stage.getIcons().add(new Image(PasswordApplication.class.getResourceAsStream("/img/logo.png")));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Form Loading Failed");
            alert.setContentText("There was an issue loading the add entry form.");
            alert.showAndWait();
        }
    }
    @FXML
    protected void handleLogout(){
        SessionManager.clearSession();
        Stage currentStage = (Stage) welcomeText.getScene().getWindow();
        currentStage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login_view.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage loginStage = new Stage();
            loginStage.setTitle("Password Manager App | Login");
            loginStage.getIcons().add(new Image(PasswordApplication.class.getResourceAsStream("/img/logo.png")));
            loginStage.setScene(loginScene);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Logout Failed");
            alert.setContentText("There was an issue logging out.");
            alert.showAndWait();
        }
    }
    @FXML
    protected void onHandleAddButton() {
        System.out.println("Add Entry clicked");
        PasswordEntry newEntry = new PasswordEntry();
        newEntry.setTitle("Example Title");
        newEntry.setUsername("exampleUser");
        newEntry.setPassword("examplePassword");
        try {
            PasswordDAO.addPassword(newEntry);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Password Added");
            alert.setContentText("The password has been successfully added.");
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
        }

}
    @FXML
    protected void onHandleEditButton() {
        PasswordEntry selectedEntry = entryTable.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_entry_form.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();

                EditEntryFormController editEntryFormController = loader.getController();
                editEntryFormController.setPasswordEntry(selectedEntry);
                editEntryFormController.setAppController(this);
                stage.setTitle("Password Manager App | Edit Entry");
                stage.getIcons().add(new Image(PasswordApplication.class.getResourceAsStream("/img/logo.png")));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "There was an issue loading the edit entry form.");
            }
        } else {
            showAlert("Error", "No entry selected.");
        }
    }
    @FXML
    private void onHandleDeleteButton() {
        PasswordEntry selectedEntry = entryTable.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this entry?");
            alert.setContentText("This action cannot be undone.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        PasswordDAO.deletePassword(selectedEntry.getId());
                        loadPasswordEntries();
                        showAlert("Success", "Password entry deleted successfully.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                        showAlert("Error", "Failed to delete the password entry.");
                    }
                }
            });
        } else {
            showAlert("Error", "No entry selected.");
        }
    }
    @FXML
    private TableView<PasswordEntry> entryTable;

    @FXML
    private TableColumn<PasswordEntry, String> titleColumn;

    @FXML
    private TableColumn<PasswordEntry, String> nameColumn;

    @FXML
    private TableColumn<PasswordEntry, String> passwordColumn;

    @FXML
    private TableColumn<PasswordEntry, String> createdColumn;

    @FXML
    private TableColumn<PasswordEntry, String> modifiedColumn;

    public void loadPasswordEntries() {
        try {
            int userId = SessionManager.getLoggedInUserId();
            List<PasswordEntry> entries = PasswordDAO.getAllPassword(userId);

            ObservableList<PasswordEntry> passwordEntries = FXCollections.observableArrayList(entries);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM, yyyy HH:mm");

            titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
            nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

            passwordColumn.setCellValueFactory(cellData -> {
                try {
                    String decryptedPassword = EncryptionUtil.decrypt(cellData.getValue().getPassword());
                    return new SimpleStringProperty(decryptedPassword);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new SimpleStringProperty("Error");
                }
            });

            createdColumn.setCellValueFactory(cellData -> {
                Date createdAt = cellData.getValue().getCreatedAt();
                return new SimpleStringProperty(formatDate(createdAt));
            });

            modifiedColumn.setCellValueFactory(cellData -> {
                Date updatedAt = cellData.getValue().getUpdatedAt();
                return new SimpleStringProperty(formatDate(updatedAt));
            });

            entryTable.setItems(passwordEntries);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load password entries.");
        }
    }


    private String formatDate(Date date) {
        if (date != null) {
            LocalDateTime dateTime = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
            return dateTime.format(DateTimeFormatter.ofPattern("EEE, dd MMM, yyyy HH:mm"));
        }
        return "";
    }
    @FXML
    public void initialize() {
        loadPasswordEntries();

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



