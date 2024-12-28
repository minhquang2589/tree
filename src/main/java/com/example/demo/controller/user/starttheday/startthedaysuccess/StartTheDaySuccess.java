package com.example.demo.controller.user.starttheday.startthedaysuccess;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import static com.example.demo.Utils.Modal.closeAllModals;

public class StartTheDaySuccess {

    @FXML
    private void closeApplication(ActionEvent event) {
        closeAllModals();
    }
}