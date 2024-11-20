package com.example.demo.config.button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

public class ButtonHandler {


    public void handleBack(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene previousScene = (Scene) stage.getUserData();
        if (previousScene != null) {
            stage.setScene(previousScene);
        }
    }

    public static void handleNavigator(ActionEvent actionEvent, String fxmlPath, Boolean isZoom) {
        try {

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = screenBounds.getWidth();
            Scene currentScene = stage.getScene();
            stage.setUserData(currentScene);
            stage.setWidth(isZoom ? (screenWidth * 0.9) : currentWidth);
            stage.setHeight(isZoom ? 800 :currentHeight);
            FXMLLoader loader = new FXMLLoader(ButtonHandler.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            double centerX = (screenBounds.getWidth() - stage.getWidth()) / 2;
            double centerY = (screenBounds.getHeight() - stage.getHeight()) / 2;
            stage.setX(centerX);
            stage.setY(centerY);
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

