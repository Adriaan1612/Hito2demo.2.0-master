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

public class LoginApplication extends Application {
    public LoginApplication() {
    }

    public void start(Stage primaryStage) {
        try {
            Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("login-view.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Inicio de Sesión");
            primaryStage.show();
        } catch (IOException var3) {
            IOException e = var3;
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
