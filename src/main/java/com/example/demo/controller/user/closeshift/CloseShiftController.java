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
    @FXML
    private void calculateTotal() {
        try {
            double total = 0;
            total += parseInput(textField10.getText()) * 500_000;
            total += parseInput(textField6.getText()) * 200_000;
            total += parseInput(textField7.getText()) * 100_000;
            total += parseInput(textField8.getText()) * 50_000;
            total += parseInput(textField5.getText()) * 20_000;
            total += parseInput(textField9.getText()) * 10_000;
            total += parseInput(textField1.getText()) * 5_000;
            total += parseInput(textField.getText()) * 2_000;
            total += parseInput(textField4.getText()) * 1_000;
            total += parseInput(textField2.getText()) * 500;
            total += parseInput(textField3.getText()) * 200;
            textField14.setText(String.format("%,.0f", total));
        } catch (NumberFormatException e) {
            textField14.setText("Lỗi nhập liệu");
        }
    }
    private double parseInput(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        return Double.parseDouble(text.replace(" ", ""));
    }
}

