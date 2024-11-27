package com.example.demo.controller.user;

import com.example.demo.Utils.Modal;
import javafx.event.ActionEvent;
import java.io.IOException;

public class SalesDashboardLayout   {
    public void onPayment(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/auth/paymentprocessing/PaymentProcessing.fxml","Chọn các hình thức thanh toán bằng cách bấm vào ô tương ứng.");
    }
}
