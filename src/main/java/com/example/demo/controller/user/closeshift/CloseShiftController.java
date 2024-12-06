package com.example.demo.controller.user.closeshift;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class CloseShiftController {

    @FXML
    private TextField textField;
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private TextField textField3;
    @FXML
    private TextField textField4;
    @FXML
    private TextField textField5;
    @FXML
    private TextField textField6;
    @FXML
    private TextField textField7;
    @FXML
    private TextField textField8;
    @FXML
    private TextField textField9;
    @FXML
    private TextField textField10;
    @FXML
    private TextField textField11;
    @FXML
    private TextField textField12;
    @FXML
    private TextField textField13;
    @FXML
    private TextField textField14;
    @FXML
    private TextField textField15;

    private TextField selectedTextField;

    @FXML
    private void selectTextField(MouseEvent event) {
        TextField clickedTextField = (TextField) event.getSource();
        selectedTextField = clickedTextField;
    }
    @FXML
    private void numberClick(ActionEvent event) {
        if (selectedTextField != null) {
            Button clickedButton = (Button) event.getSource();
            String buttonText = clickedButton.getText();
            selectedTextField.appendText(buttonText);
        }
    }

    @FXML
    private void clearText(ActionEvent event) {
        if (selectedTextField != null) {
            selectedTextField.clear();
        }
    }
    @FXML
    private void clearText1(ActionEvent event) {
        if (selectedTextField != null && !selectedTextField.getText().isEmpty()) {
            String currentText = selectedTextField.getText();
            if (currentText.length() > 0) {
                selectedTextField.deleteText(currentText.length() - 1, currentText.length());
            }
        }
    }
}

