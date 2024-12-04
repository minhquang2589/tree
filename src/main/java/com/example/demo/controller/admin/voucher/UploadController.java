package com.example.demo.controller.admin.voucher;

import com.example.demo.config.MySQLConnection;
import com.example.demo.model.Voucher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.example.demo.Utils.Modal.showAlert;

public class UploadController {
    @FXML
    public TextField voucherCodeField;
    @FXML
    public TextField voucherPercentageField;
    @FXML
    public TextField voucherQuantityField;
    @FXML
    public DatePicker startDatePicker;
    @FXML
    public DatePicker endDatePicker;

    @FXML
    public void handleUpload(ActionEvent actionEvent) throws SQLException {
        String voucherCode = voucherCodeField.getText();
        String voucherPercentageText = voucherPercentageField.getText();
        String voucherQuantityText = voucherQuantityField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (voucherCode.isEmpty() || voucherPercentageText.isEmpty() || voucherQuantityText.isEmpty() ||
                startDate == null || endDate == null) {
            showAlert("Vui lòng nhập đủ thông tin.");
            return;
        }
        try {
            double voucherPercentage = Double.parseDouble(voucherPercentageText);
            int voucherQuantity = Integer.parseInt(voucherQuantityText);

            Voucher newVoucher = new Voucher(0, voucherCode, voucherPercentage, voucherQuantity,
                    startDate, endDate, "active");

            if (insertVoucher(newVoucher)) {
                showAlert("Tải lên phiếu giảm giá thành công!");
            } else {
                showAlert("Có lỗi xảy ra khi tải lên.");
            }

        } catch (NumberFormatException e) {
            showAlert("Vui lòng nhập số hợp lệ cho phần trăm và số lượng.");
        }
    }

    private boolean insertVoucher(Voucher voucher) throws SQLException {
        String query = "INSERT INTO vouchers (voucher_code, voucher_percentage, voucher_quantity, " +
                "start_date, end_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = MySQLConnection.connect()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, voucher.getVoucherCode());
                preparedStatement.setDouble(2, voucher.getVoucherPercentage());
                preparedStatement.setInt(3, voucher.getVoucherQuantity());
                preparedStatement.setDate(4, java.sql.Date.valueOf(voucher.getStartDate()));
                preparedStatement.setDate(5, java.sql.Date.valueOf(voucher.getEndDate()));
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        }
    }
}
