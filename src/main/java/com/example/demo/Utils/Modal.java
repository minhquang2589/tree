package com.example.demo.Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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
}
