package com.example.demo;

import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
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

import static com.example.demo.controller.user.LoginController.*;

public class MyApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        Connection connect = MySQLConnection.connect();
        if (connect == null) {
            Modal.showAlert(null, "Không thể kết nối với cơ sở dữ liệu. Xin vui lòng thử lại sau!",
                    Alert.AlertType.ERROR, null, null);
            return;
        }
        boolean isLoggedIn = false;
        String fxmlPath = handleCheckRole();
        if (fxmlPath.equals("/com/example/demo/controller/auth/login-view.fxml")) {
            PreferencesUtils.clearUser();
        }else{
            isLoggedIn = true;
        }
        loadScene(stage, fxmlPath, isLoggedIn);
    }

    private void loadScene(Stage stage, String fxmlFilePath, boolean isLoggedIn) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource(fxmlFilePath));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 600);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double windowWidth = screenWidth * (isLoggedIn ? 0.9 : 0.46);
        stage.setWidth(windowWidth);
        stage.setHeight(isLoggedIn ? 800 : 550);
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
