package com.example.demo.controller.user.pay;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
public class paycontroller {
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private TextField textField3;
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
    private Button buttonC;
    @FXML
    private Button buttonBackspace;
    @FXML
    private Button buttonQL;
    @FXML
    private Button buttonXN;

    private TextField currentTextField;


    @FXML
    public void initialize() {
        textField1.setOnMouseClicked(e -> currentTextField = textField1);
        textField2.setOnMouseClicked(e -> currentTextField = textField2);
        textField3.setOnMouseClicked(e -> currentTextField = textField3);
        textField1.setFocusTraversable(false);
        textField2.setFocusTraversable(false);
        textField3.setFocusTraversable(false);
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
        buttonC.setFocusTraversable(false);
        buttonBackspace.setFocusTraversable(false);
        buttonQL.setFocusTraversable(false);
        buttonXN.setFocusTraversable(false);

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
        if (currentTextField != null) {
            currentTextField.clear();
        }
    }
    public void handleCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/controller/auth/view/user/PaymentProcessing/PaymentProcessing.fxml"));
            Scene previousScene = new Scene(loader.load());
            Stage currentStage = (Stage) textField1.getScene().getWindow();
            currentStage.setScene(previousScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleConfirm(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/controller/auth/view/user/QRcode/QR-code.fxml"));
            Parent newRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(newRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
