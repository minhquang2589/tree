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
    @FXML
    private Button button0;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button button7;
    @FXML
    private Button button8;
    @FXML
    private Button button9;
    @FXML
    private Button button00;
    @FXML
    private Button button000;
    @FXML
    private Button buttonxoa;
    @FXML
    private Button buttonc;
    @FXML
    private Button buttonnhap;
    @FXML
    private Button buttonok;


    private TextField selectedTextField;



    @FXML
    private void initialize() {
        textField.setFocusTraversable(false);
        textField1.setFocusTraversable(false);
        textField2.setFocusTraversable(false);
        textField3.setFocusTraversable(false);
        textField4.setFocusTraversable(false);
        textField5.setFocusTraversable(false);
        textField6.setFocusTraversable(false);
        textField7.setFocusTraversable(false);
        textField8.setFocusTraversable(false);
        textField9.setFocusTraversable(false);
        textField10.setFocusTraversable(false);
        textField11.setFocusTraversable(false);
        textField12.setFocusTraversable(false);
        textField13.setFocusTraversable(false);
        textField14.setFocusTraversable(false);
        textField15.setFocusTraversable(false);
        button0.setFocusTraversable(false);
        button1.setFocusTraversable(false);
        button2.setFocusTraversable(false);
        button3.setFocusTraversable(false);
        button4.setFocusTraversable(false);
        button5.setFocusTraversable(false);
        button6.setFocusTraversable(false);
        button7.setFocusTraversable(false);
        button8.setFocusTraversable(false);
        button9.setFocusTraversable(false);
        button00.setFocusTraversable(false);
        button000.setFocusTraversable(false);
        buttonxoa.setFocusTraversable(false);
        buttonc.setFocusTraversable(false);
        buttonnhap.setFocusTraversable(false);
        buttonok.setFocusTraversable(false);
    }

    @FXML
    private void selectTextField(MouseEvent event) {
        selectedTextField = (TextField) event.getSource();
    }
    @FXML
    private void numberClick(ActionEvent event) {
        if (selectedTextField == null) {
            return;
        }
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        selectedTextField.appendText(buttonText);
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

