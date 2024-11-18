package com.example.demo;

import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.model.UserModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import com.example.demo.config.MySQLConnection;
import javafx.scene.Parent;
import java.io.IOException;

public class MyApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        String fxmlFilePath = "/com/example/demo/controller/auth/login-view.fxml";
        MySQLConnection.connect();
        boolean isLoggedIn = (boolean) PreferencesUtils.get("isLoggedIn", false);
        UserModel user = PreferencesUtils.getUser();

        if (isLoggedIn && user != null) {
            fxmlFilePath = "/com/example/demo/controller/auth/view/admin/dashboard-layout.fxml";
            System.out.println(user.toString());
        }

        loadScene(stage, fxmlFilePath, isLoggedIn);
    }

    public void reloadScene(Stage stage, String fxmlFilePath, boolean isLoggedIn) throws IOException {
        loadScene(stage, fxmlFilePath, isLoggedIn);
    }

    private void loadScene(Stage stage, String fxmlFilePath, boolean isLoggedIn) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource(fxmlFilePath));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 620, 500);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double windowWidth = screenWidth * (isLoggedIn ? 0.9 : 0.6);
        stage.setWidth(windowWidth);
        stage.setHeight(isLoggedIn ? 800 : 500);

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        MySQLConnection.close();
    }

    public static void main(String[] args) {
        launch();
    }
}
