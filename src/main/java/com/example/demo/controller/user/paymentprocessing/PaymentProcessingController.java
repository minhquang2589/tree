package com.example.demo.controller.user.paymentprocessing;

import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import java.io.IOException;


public class PaymentProcessingController {
    public void onCashPaymentButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/controller/auth/view/user/CashTM/Cash-TM.fxml"));
        Node newScene = loader.load();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setTitle("Thanh Toán Tiền Mặt.");
        Scene scene = new Scene((Parent) newScene);
        currentStage.setScene(scene);
        currentStage.show();
    }
    public  void  handleTpayQrcode (ActionEvent event) throws  IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/controller/auth/view/user/QRPayment/QR-Payment.fxml"));
        Node newScene = loader.load();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setTitle("Thanh Toán QRCode.");
        Scene scene = new Scene((Parent) newScene);
        currentStage.setScene(scene);
        currentStage.show();
    }
}
