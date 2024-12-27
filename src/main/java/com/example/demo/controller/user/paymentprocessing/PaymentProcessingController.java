package com.example.demo.controller.user.paymentprocessing;
import com.example.demo.classInterFace.setDataInterface;
import com.example.demo.model.ProductSearch;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

import static com.example.demo.Utils.Modal.showModalWithData;

public class PaymentProcessingController implements setDataInterface<ObservableList<ProductSearch>> {

    @Override
    public void setData(ObservableList<ProductSearch> data) {
        System.out.println(data);
    }

    @FXML
    public void handleCashPayment(ActionEvent event) throws IOException {

        showModalWithData("/com/example/demo/controller/auth/view/user/cash/cash-tt.fxml", "", null, () -> {
            System.out.println("call back handle");
        });
    }

    @FXML
    private void handleTpayQrcode(ActionEvent event) throws IOException {
        showModalWithData("/com/example/demo/controller/auth/view/user/pay/e-walletpayment.fxml", "", null, () -> {
            System.out.println("call back handle QR Pay");
        });
    }

}
