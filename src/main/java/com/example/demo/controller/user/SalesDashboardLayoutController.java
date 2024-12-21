package com.example.demo.controller.user;

import com.example.demo.Utils.Config;
import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.model.Shift;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.example.demo.Utils.Modal.showModal;
import static com.example.demo.config.MySQLConnection.connect;
import static com.example.demo.config.button.ButtonHandler.handleNavigator;

import com.example.demo.controller.user.starttheday.StartTheDayController;

public class SalesDashboardLayoutController {

    @FXML
    public Button sl_sales;

    @FXML
    public Button sl_sales2;

    @FXML
    public Button sl_CancelTransaction;

    @FXML
    public Button sl_Payment;


    public void onSales(ActionEvent actionEvent) throws IOException {
        StartTheDayController startTheDayController = new StartTheDayController();
        if (!startTheDayController.check_day()) {
            sl_sales.setVisible(false);
            sl_sales2.setVisible(false);
            sl_CancelTransaction.setVisible(true);
            sl_Payment.setVisible(true);
        } else {
            Modal.showAlert(
                    "Thông báo",
                    "Chưa bắt đầu ngày",
                    Alert.AlertType.INFORMATION,
                    null, null
            );
        }
    }

    public void onPayment(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/paymentprocessing/paymentProcessing.fxml", "Chọn các hình thức thanh toán bằng cách bấm vào ô tương ứng.", null);
    }

    public void onOrderList(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/orderlist/order-list.fxml", "Danh sách đơn hàng", null);
    }

    public void onReport(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/report/report.fxml", "Báo cáo nhanh", null);
    }

    public void onTranslation(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/translation/translation.fxml", "Dịch vụ", null);
    }

    public void onMember(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/member/member.fxml", "Thông tin khách hàng", null);
    }

    public void onCheckPrice(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/checkprice/checkprice.fxml", "Kiểm tra giá sản phẩm", null);
    }

    public void onOut(ActionEvent actionEvent) {
        PreferencesUtils.clearUser();
        handleNavigator(actionEvent, "/com/example/demo/controller/auth/login-view.fxml", true);
    }

    public void onCloseshift(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/closeshift/closeshift.fxml", "Kết thúc ca",this::Countshift );

    }

    public void onStartTheDay(ActionEvent actionEvent) throws IOException {
        StartTheDayController startTheDayController = new StartTheDayController();
        if (startTheDayController.check_day()) {
            Modal.showModal("/com/example/demo/controller/auth/view/user/starttheday/starttheday.fxml", "Bắt đầu ngày", null);
        } else {
            Modal.showAlert("Đã có người check in");
        }
    }

    public void onExitApplication(ActionEvent event) {
        Modal.showAlert(
                "Xác nhận thoát",
                "Bạn có chắc chắn muốn thoát ứng dụng?",
                Alert.AlertType.CONFIRMATION,
                () -> closeApplication(event), null
        );
    }

    private void closeApplication(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private TextField displayField;


    @FXML
    private void onNumberClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        displayField.appendText(buttonText);
    }

    @FXML
    private void onClear(ActionEvent event) {
        String currentText = displayField.getText();
        if (currentText != null && !currentText.isEmpty()) {
            displayField.setText(currentText.substring(0, currentText.length() - 1));
        }
    }

    @FXML
    public TextField saleshiftnumber;

    @FXML
    public void Countshift() {
        List<Shift> shifts = PreferencesUtils.getShiftList();
        int count = shifts.size();
        if(count > 0) {
            saleshiftnumber.setText(String.valueOf(count + 1));
        }else{
            saleshiftnumber.setText("0");
        }
    }


    public void initialize() throws IOException {
        Countshift();
    }
}


