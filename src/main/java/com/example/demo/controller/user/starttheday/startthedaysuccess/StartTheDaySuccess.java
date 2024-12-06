package com.example.demo.controller.user.starttheday.startthedaysuccess;

import com.example.demo.model.StartDay;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.time.LocalDate;

public class StartTheDaySuccess {

    @FXML
    private void closeApplication(ActionEvent event) {
        // Update the StartDay model
        StartDay.setStartDay(true);
        // Close the window
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}