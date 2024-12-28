package com.example.demo.controller.user.paymentprocessing;
import com.example.demo.classInterFace.initDataInterface;
import com.example.demo.model.ProductSearch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Map;

import static com.example.demo.Utils.Config.calculateCartTotal;
import static com.example.demo.Utils.Config.formatCurrencyVND;
import static com.example.demo.Utils.Modal.closeModal;
import static com.example.demo.Utils.Modal.showModalWithData;

public class PaymentProcessingController implements initDataInterface<ObservableList<ProductSearch>> {
    @FXML
    public TextField totalPrice;
    @FXML
    public TextField totalQuantity;
    @FXML
    private ObservableList<ProductSearch> productList = FXCollections.observableArrayList();
    Map<String, Double> cart = null;

    @Override
    public void initData(ObservableList<ProductSearch> data) {
        cart = calculateCartTotal(data, null);
        totalPrice.setText(formatCurrencyVND(cart.get("totalAmount")));
        double totalQuantityD = cart.get("totalQuantity");
        int totalQuantityInt = (int) totalQuantityD;
        totalQuantity.setText(totalQuantityInt + " " + "Sản phẩm");
        productList = data;
    }


    @FXML
    public void handleCashPayment(ActionEvent event) throws IOException {

        showModalWithData("/com/example/demo/controller/auth/view/user/cash/cash-tt.fxml", "Thanh toán tiền mặt", productList, () -> {

        });
    }

    @FXML
    private void handleTpayQrcode(ActionEvent event) throws IOException {
        showModalWithData("/com/example/demo/controller/auth/view/user/pay/e-walletpayment.fxml", "Thanh toán QR code", productList, () -> {
            System.out.println("call back QR handle");
        });
    }

}
