package com.example.demo.controller.user.paymentprocessing;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class PaymentProcessingController {
    @FXML
    public void handleCashPayment(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/controller/auth/view/user/cash/cash-tt.fxml"));
            Parent newRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(newRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleTpayQrcode(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/controller/auth/view/user/pay/e-walletpayment.fxml"));
            Parent newRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(newRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
