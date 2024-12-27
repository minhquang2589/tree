package com.example.demo.controller.user.QR;

import javafx.scene.control.Button;

import static com.example.demo.Utils.Modal.closeModal;

public class QRcodecontroller {

    public Button buttonCancel;

    public void handleCancel(javafx.event.ActionEvent event) {
        closeModal();
    }
}
