package com.example.demo;

import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.model.UserModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;
import com.example.demo.config.MySQLConnection;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static com.example.demo.controller.HomeController.handleLogin;

public class MyApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        Connection connect = MySQLConnection.connect();
        if (connect == null) {
            Modal.showAlert(null, "Không thể kết nối với cơ sở dữ liệu. Xin vui lòng thử lại sau!",
                    Alert.AlertType.ERROR, null, null);
            return;
        }
        String fxmlPath = "/com/example/demo/controller/auth/login-view.fxml";
        boolean isLoggedIn = (boolean) PreferencesUtils.get("isLoggedIn", false);
        UserModel user = PreferencesUtils.getUser();
        if (isLoggedIn && user != null) {
            System.out.println(user.toString());
            UserModel login = handleLogin(user.getPhone(), user.getPassword());
            if (login != null) {
                fxmlPath = "/com/example/demo/controller/auth/view/admin/dashboard-layout.fxml";
            }
        }
        loadScene(stage, fxmlPath, isLoggedIn);
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
