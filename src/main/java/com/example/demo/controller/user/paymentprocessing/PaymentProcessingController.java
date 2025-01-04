package com.example.demo.controller.user.paymentprocessing;

import com.example.demo.classInterFace.initDataInterface;
import com.example.demo.config.MySQLConnection;
import com.example.demo.model.ProductSearch;
import com.example.demo.model.Voucher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Map;

import static com.example.demo.Utils.Config.calculateCartTotal;
import static com.example.demo.Utils.Config.formatCurrencyVND;
import static com.example.demo.Utils.Modal.*;
import static com.example.demo.controller.admin.voucher.VoucherController.applyVoucher;

public class PaymentProcessingController implements initDataInterface<ObservableList<ProductSearch>> {
    @FXML
    public TextField totalPrice;
    @FXML
    public TextField totalQuantity;
    @FXML

    public TextField voucherCodeField;
    @FXML
    public Label voucherStatusLabel;
    @FXML
    public Button buttonText;
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
    public void handleCashPayment(ActionEvent event) throws IOException, SQLException {

        showModalWithData("/com/example/demo/controller/auth/view/user/cash/cash-tt.fxml", "Thanh toán tiền mặt", productList, () -> {

        });
    }

    @FXML
    private void handleTpayQrcode(ActionEvent event) throws IOException, SQLException {
        showModalWithData("/com/example/demo/controller/auth/view/user/pay/e-walletpayment.fxml", "Thanh toán QR code", productList, () -> {
            System.out.println("call back QR handle");
        });
    }

    public void voucherOnClick(ActionEvent actionEvent) throws IOException {
        showModal("/com/example/demo/controller/auth/view/user/paymentprocessing/voucher-view.fxml", "", () -> {
            if (applyVoucher != null) {
                cart = calculateCartTotal(productList, applyVoucher);
                totalPrice.setText(formatCurrencyVND(cart.get("totalAmount")));
                double totalQuantityD = cart.get("totalQuantity");
                int totalQuantityInt = (int) totalQuantityD;
                totalQuantity.setText(totalQuantityInt + " " + "Sản phẩm");
            }
        });
    }


    @FXML
    private void onApplyVoucher(ActionEvent event) {

        String voucherCode = voucherCodeField.getText();
        if (voucherCode.isEmpty()) {
            voucherStatusLabel.setText("Vui lòng nhập mã voucher.");
        } else {
            Voucher voucher = checkVoucher(voucherCode);
            if (voucher != null) {
                voucherStatusLabel.setText(null);
                showAlert(
                        "Xác nhận áp dụng voucher?",
                        "Bạn có muốn áp dụng mã Voucher này?, Mã: " + voucher.getVoucherCode() +
                                " giảm giá " + voucher.getVoucherPercentage() + " % " + " số lượng còn lại của mã này là " + voucher.getVoucherQuantity() + " cho đến ngày " + voucher.getEndDate(),
                        Alert.AlertType.CONFIRMATION,
                        () -> {
                            applyVoucher = voucher;
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            closeModal(stage);
                        }, null
                );
            } else {
                voucherStatusLabel.setText("Mã voucher không hợp lệ.");
            }
        }
    }


    public static Voucher checkVoucher(String voucherCode) {
        String query = """
                SELECT voucher_code, voucher_percentage, voucher_quantity, start_date, end_date, status,voucher_id
                FROM vouchers
                WHERE voucher_code = ? AND status = 'active' AND CURDATE() BETWEEN start_date AND end_date AND voucher_quantity > 0;
                """;
        Connection connection = MySQLConnection.connect();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, voucherCode);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String code = rs.getString("voucher_code");
                    int id = rs.getInt("voucher_id");
                    double percentage = rs.getDouble("voucher_percentage");
                    int quantity = rs.getInt("voucher_quantity");
                    Date startDate = rs.getDate("start_date");
                    Date endDate = rs.getDate("end_date");
                    String status = rs.getString("status");
                    return new Voucher(id, code, percentage, quantity, startDate.toLocalDate(), endDate.toLocalDate(), status);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
