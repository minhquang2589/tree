package com.example.demo.controller.user.QR;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import static com.example.demo.Utils.Modal.closeModal;

public class QRcodecontroller {

    public Button buttonCancel;

    public void handleCancel(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        closeModal(stage);
    }
}
