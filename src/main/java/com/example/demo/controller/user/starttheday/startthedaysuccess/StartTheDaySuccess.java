package com.example.demo.controller.user.starttheday.startthedaysuccess;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import static com.example.demo.Utils.Modal.closeModal;

public class StartTheDaySuccess {

    @FXML
    private void closeApplication(ActionEvent event) {
        closeModal();
    }
}