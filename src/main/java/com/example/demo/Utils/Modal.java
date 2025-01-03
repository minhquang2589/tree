package com.example.demo.Utils;

import com.example.demo.classInterFace.initDataInterface;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Modal {

    public static List<Stage> modalStages = new ArrayList<>();

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
        FXMLLoader loader = new FXMLLoader(Modal.class.getResource(fxmlPath));
        Parent root = loader.load();
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle(title);
        modalStage.setScene(new Scene(root));
        modalStage.setResizable(true);

        modalStages.add(modalStage);

        Platform.runLater(() -> {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double centerX = (screenBounds.getWidth() - modalStage.getWidth()) / 2;
            double centerY = (screenBounds.getHeight() - modalStage.getHeight()) / 2;
            modalStage.setX(centerX);
            modalStage.setY(centerY);
        });

        modalStage.setOnHidden(event -> {
            modalStages.remove(modalStage);
            if (onCallback != null) {
                onCallback.run();
            }
        });

        modalStage.showAndWait();
    }

    public static void closeModal(Stage stage) {
        if (stage != null) {
            stage.close();
            modalStages.remove(stage);
        }
    }

    public static void closeAllModals() {
        for (Stage stage : new ArrayList<>(modalStages)) {
            stage.close();
        }
        modalStages.clear();
    }

    public static <T> void showModalWithData(String fxmlPath, String title, T data, Runnable onCallback) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(Modal.class.getResource(fxmlPath));
        Parent root = loader.load();
        Object controller = loader.getController();
        if (controller instanceof initDataInterface<?>) {
            initDataInterface<T> dataController = (initDataInterface<T>) controller;
            dataController.initData(data);
        }

        Stage stage = new Stage();
        stage.setResizable(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        modalStages.add(stage);
        stage.setOnHidden(event -> {
            modalStages.remove(stage);
            if (onCallback != null) {
                onCallback.run();
            }
        });

        stage.show();
    }
}
