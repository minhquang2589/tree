package com.example.demo.controller.user;

import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import static com.example.demo.config.button.ButtonHandler.handleNavigator;

public class SalesDashboardLayoutController {

    @FXML
    public Button sl_sales;

    @FXML
    public Button sl_sales2;

    @FXML
    public Button sl_CancelTransaction;

    @FXML
    public Button sl_Payment;

    @FXML
    public Button searchButton;

    @FXML
    public TextField searchTextFiled;

    @FXML
    private TextField startDateField;


    public void onSales(ActionEvent actionEvent) throws IOException {
        sl_sales.setVisible(false);
        sl_sales2.setVisible(false);
        sl_CancelTransaction.setVisible(true);
        sl_Payment.setVisible(true);

    }

    public void onPayment(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/paymentprocessing/paymentProcessing.fxml", "Chọn các hình thức thanh toán bằng cách bấm vào ô tương ứng.");
    }

    public void onOrderList(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/orderlist/order-list.fxml", "Danh sách đơn hàng");
    }

    public void onReport(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/report/report.fxml", "Báo cáo nhanh");
    }

    public void onTranslation(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/translation/translation.fxml", "Dịch vụ");
    }

    public void onMember(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/member/member.fxml", "Thông tin khách hàng");
    }

    public void onCheckPrice(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/checkprice/checkprice.fxml", "Kiểm tra giá sản phẩm");
    }

    public void onOut(ActionEvent actionEvent) {
        PreferencesUtils.clearUser();
        handleNavigator(actionEvent, "/com/example/demo/controller/auth/login-view.fxml", true);
    }

    public void onCloseshift(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/closeshift/closeshift.fxml", "Kết thúc ca");

    }

    public void onStartTheDay(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/starttheday/starttheday.fxml", "Bắt đầu ngày");
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
    private TextField salesDateField;

    private static SalesDashboardLayoutController instance;
    public SalesDashboardLayoutController() {
        instance = this;
    }

    public static SalesDashboardLayoutController getInstance() {
        return instance;
    }
    public void updateSalesDateField(LocalDate date) {
        if (date != null) {
            salesDateField.setText(date.toString());
        }
    }
}


