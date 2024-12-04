package com.example.demo.config.button;

import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import static com.example.demo.controller.LoginController.handleCheckRole;

public class ButtonHandler {

    public void handleBack(ActionEvent actionEvent) throws SQLException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene previousScene = (Scene) stage.getUserData();
        if (previousScene != null) {
            stage.setUserData(null);
            stage.setScene(previousScene);
        } else {
            User user = PreferencesUtils.getUser();
            handleNavigator(actionEvent, handleCheckRole(user), true);
        }
    }


    public static void handleNavigator(ActionEvent actionEvent, String fxmlPath, Boolean isZoom) {
        try {
            Stage stage;
            if (fxmlPath.equals("/com/example/demo/controller/auth/login-view.fxml")) {
                stage = new Stage();
                stage.setWidth(700);
                stage.setHeight(550);
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((screenBounds.getWidth() - 700) / 2);
                stage.setY((screenBounds.getHeight() - 550) / 2);
                ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
            } else {
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setUserData(stage.getScene());
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                double currentWidth = stage.getWidth();
                double currentHeight = stage.getHeight();
                double screenWidth = screenBounds.getWidth();
                stage.setWidth(isZoom ? (screenWidth * 0.9) : currentWidth);
                stage.setHeight(isZoom ? 800 : currentHeight);
                double centerX = (screenBounds.getWidth() - stage.getWidth()) / 2;
                double centerY = (screenBounds.getHeight() - stage.getHeight()) / 2;
                stage.setX(centerX);
                stage.setY(centerY);
            }
            FXMLLoader loader = new FXMLLoader(ButtonHandler.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

