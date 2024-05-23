package com.empresa.hito2demo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    private LoginListener loginListener;
    private String registeredUsername;
    private String registeredPassword;

    public LoginController() {
    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    public void setRegisteredCredentials(String username, String password) {
        this.registeredUsername = username;
        this.registeredPassword = password;
    }

    @FXML
    private void handleLogin() {
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        boolean isValid = this.validCredentials(username, password);
        if (isValid && this.loginListener != null) {
            this.loginListener.onLoginSuccess();
            this.closeLoginWindow();
        } else {
            showInvalidCredentialsAlert();
        }
    }

    private boolean validCredentials(String username, String password) {
        return username.equals(this.registeredUsername) && password.equals(this.registeredPassword);
    }

    private void closeLoginWindow() {
        Stage stage = (Stage)this.usernameField.getScene().getWindow();
        stage.close();
    }

    private void showInvalidCredentialsAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de inicio de sesión");
        alert.setHeaderText(null);
        alert.setContentText("El nombre de usuario o la contraseña son incorrectos. Por favor, inténtelo de nuevo.");
        alert.showAndWait();
    }
}

