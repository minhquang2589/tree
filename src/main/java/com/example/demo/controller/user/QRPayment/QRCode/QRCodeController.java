package com.example.demo.controller.user.QRPayment.QRCode;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class QRCodeController {
    public void handleCancel(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/controller/auth/view/user/QRPayment/QR-Payment.fxml"));
        Node newScene = loader.load();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setTitle("Thanh Toán QRCode.");
        Scene scene = new Scene((Parent) newScene);
        currentStage.setScene(scene);
        currentStage.show();
    }
}
