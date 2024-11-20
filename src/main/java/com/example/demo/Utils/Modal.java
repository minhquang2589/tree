package com.example.demo.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Modal {

    public static void showAlert(String title, String message, Alert.AlertType type, Runnable onOkAction, Runnable onCancelAction) {
        Alert alert = new Alert(type != null ? type : Alert.AlertType.NONE);
        alert.setTitle(title != null ? title : "Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message != null ? message : "Lỗi. Xin vui lòng thử lại sau");
        if (type == null) {
            alert.getButtonTypes().add(ButtonType.CLOSE);
        }
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (onOkAction != null) {
                    onOkAction.run();
                }
            } else if (response == ButtonType.CANCEL) {
                if (onCancelAction != null) {
                    onCancelAction.run();
                }
            }
        });
    }

    public static void showAlert(String message) {
        showAlert("Thông báo", message != null ? message : "Lỗi. Xin vui lòng thử lại sau", Alert.AlertType.INFORMATION, null, null);
    }

    public static void showModal(String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(Modal.class.getResource(fxmlPath));
        Parent root = loader.load();
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle(title);
        modalStage.setScene(new Scene(root));
        modalStage.setResizable(true);
        modalStage.showAndWait();
    }
}
