package com.example.demo.controller.user.cash;

import com.example.demo.classInterFace.initDataInterface;
import com.example.demo.model.ProductSearch;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.util.Map;

import static com.example.demo.Utils.Config.calculateCartTotal;
import static com.example.demo.Utils.Config.formatCurrencyVND;
import static com.example.demo.Utils.Modal.closeModal;

public class CashController implements initDataInterface<ObservableList<ProductSearch>> {
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;

    private TextField currentTextField;
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
    private Button buttonOK;
    @FXML
    private Button buttonCancel;
    @FXML
    private Button buttonBackspace;
    @FXML
    private Button buttonC;

    Map<String, Double> cart = null;


    @Override
    public void initData(ObservableList<ProductSearch> data) {
        cart = calculateCartTotal(data, null);
        textField1.setText(formatCurrencyVND(cart.get("totalAmount")));
        textField2.setText(textField1.getText());
    }


    @FXML
    public void initialize() {
        textField1.setOnMouseClicked(e -> currentTextField = textField1);
        textField2.setOnMouseClicked(e -> currentTextField = textField2);
        textField1.setFocusTraversable(false);
        textField2.setFocusTraversable(false);
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
        buttonCancel.setFocusTraversable(false);
        buttonBackspace.setFocusTraversable(false);
        buttonC.setFocusTraversable(false);
        buttonOK.setFocusTraversable(false);
    }

    public void handleButtonPress(ActionEvent event) {
        String buttonText = ((javafx.scene.control.Button) event.getSource()).getText();
        if (currentTextField != null) {
            currentTextField.appendText(buttonText);
        }
    }

    public void handleBackspace(ActionEvent event) {
        if (currentTextField != null && currentTextField.getLength() > 0) {
            currentTextField.deleteText(currentTextField.getLength() - 1, currentTextField.getLength());
        }
    }

    public void handleClear(ActionEvent event) {
        currentTextField.clear();
    }

    public void handleCancel(ActionEvent event) {
        closeModal();
    }


}
