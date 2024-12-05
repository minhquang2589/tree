package com.example.demo.Utils;

import com.example.demo.classInterFace.setDataInterface;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Modal {

    private static Stage currentModalStage;

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

    public static void showAlert(String message, Runnable onOkAction) {
        showAlert("Thông báo", message, Alert.AlertType.INFORMATION, onOkAction, null);
    }

    public static void showAlert(String message) {
        showAlert("Thông báo", message != null ? message : "Lỗi. Xin vui lòng thử lại sau", Alert.AlertType.INFORMATION, null, null);
    }

    public static void showModal(String fxmlPath, String title , Runnable onCallback) throws IOException {
        if (currentModalStage != null && currentModalStage.isShowing()) {
            currentModalStage.close();
        }
        FXMLLoader loader = new FXMLLoader(Modal.class.getResource(fxmlPath));
        Parent root = loader.load();
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle(title);
        modalStage.setScene(new Scene(root));
        modalStage.setResizable(true);
        currentModalStage = modalStage;
        Platform.runLater(() -> {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double centerX = (screenBounds.getWidth() - modalStage.getWidth()) / 2;
            double centerY = (screenBounds.getHeight() - modalStage.getHeight()) / 2;
            modalStage.setX(centerX);
            modalStage.setY(centerY);
        });
        modalStage.setOnHidden(event -> {
            if (onCallback != null) {
                onCallback.run();
            }
        });
        modalStage.showAndWait();
    }

    public static void closeModal() {
        if (currentModalStage != null) {
            currentModalStage.close();
            currentModalStage = null;
        }
    }

    public static <T> void showModalWithData(String fxmlPath,  String title,T model,Runnable onCallback) throws IOException {
        FXMLLoader loader = new FXMLLoader(Modal.class.getResource(fxmlPath));
        Parent root = loader.load();

        Object controller = loader.getController();
        if (controller instanceof setDataInterface) {
            setDataInterface<T> dataController = (setDataInterface<T>) controller;
            dataController.setData(model);
        }

        Stage stage = new Stage();
        stage.setResizable(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setOnHidden(event -> {
            if (onCallback != null) {
                onCallback.run();
            }
        });
        stage.show();
    }

}
