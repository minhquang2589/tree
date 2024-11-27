package com.example.demo.controller.user;

import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.config.button.ButtonHandler;
import com.example.demo.config.loading.LoadingOverlay;
import com.example.demo.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.SQLException;

import static com.example.demo.config.button.ButtonHandler.handleNavigator;


public class SalesDashboardLayout   {
    public void onPayment(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/auth/paymentprocessing/PaymentProcessing.fxml","Chọn các hình thức thanh toán bằng cách bấm vào ô tương ứng.");
    }
}
