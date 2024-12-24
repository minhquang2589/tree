package com.example.demo.controller.user.paymentprocessing;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

import static com.example.demo.Utils.Modal.showModal;

public class PaymentProcessingController {
    @FXML
    public void handleCashPayment(ActionEvent event) throws IOException {
        showModal("/com/example/demo/controller/auth/view/user/cash/cash-tt.fxml", "",null);

//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/controller/auth/view/user/cash/cash-tt.fxml"));
//            Parent newRoot = loader.load();
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.setScene(new Scene(newRoot));
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    @FXML
    private void handleTpayQrcode(ActionEvent event) throws IOException {
        showModal("/com/example/demo/controller/auth/view/user/pay/e-walletpayment.fxml", "",null);

//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/controller/auth/view/user/pay/e-walletpayment.fxml"));
//            Parent newRoot = loader.load();
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.setScene(new Scene(newRoot));
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
