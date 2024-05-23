//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.empresa.hito2demo;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private String registeredUsername;
    private String registeredPassword;

    public HelloApplication() {
    }

    public void start(Stage stage) throws IOException {
        this.showRegisterView(stage);
    }

    private void showRegisterView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/empresa/hito2demo/register-view.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 400.0, 300.0);
        stage.setTitle("Register");
        stage.setScene(scene);
        stage.show();
        RegisterController registerController = (RegisterController)fxmlLoader.getController();
        registerController.setRegisterListener((username, password) -> {
            this.registeredUsername = username;
            this.registeredPassword = password;

            try {
                this.showLoginView(stage);
            } catch (IOException var5) {
                IOException e = var5;
                e.printStackTrace();
            }

        });
    }

    private void showLoginView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/empresa/hito2demo/login-view.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 400.0, 300.0);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        LoginController loginController = (LoginController)fxmlLoader.getController();
        loginController.setLoginListener(() -> {
            try {
                this.showMainView(stage);
            } catch (IOException var3) {
                IOException e = var3;
                e.printStackTrace();
            }

        });
        loginController.setRegisteredCredentials(this.registeredUsername, this.registeredPassword);
    }

    private void showMainView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/empresa/hito2demo/hello-view.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 1000.0, 800.0);
        stage.setTitle("User Form");
        stage.setScene(scene);
        stage.show();
        HelloController helloController = (HelloController)fxmlLoader.getController();
        stage.setOnCloseRequest((event) -> {
            helloController.close();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
