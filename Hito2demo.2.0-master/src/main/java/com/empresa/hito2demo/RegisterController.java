//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.empresa.hito2demo;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    private RegisterListener registerListener;

    public RegisterController() {
    }

    public void setRegisterListener(RegisterListener listener) {
        this.registerListener = listener;
    }

    @FXML
    private void handleRegister() {
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        if (this.registerListener != null) {
            this.registerListener.onRegisterSuccess(username, password);
            this.closeRegisterWindow();
        }

    }

    private void closeRegisterWindow() {
        Stage stage = (Stage)this.usernameField.getScene().getWindow();
        stage.close();
    }
}
