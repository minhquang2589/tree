package com.example.demo.controller.admin.voucher;
import com.example.demo.classInterFace.ModalController;
import com.example.demo.classInterFace.initDataInterface;
import com.example.demo.config.MySQLConnection;
import com.example.demo.model.Voucher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.example.demo.Utils.Modal.showAlert;

public class UpdateController implements initDataInterface<Voucher> {


    @FXML
    private ComboBox statusComboBox;
    @FXML
    private TextField voucherCodeField;

    @FXML
    private TextField voucherPercentageField;

    @FXML
    private TextField voucherQuantityField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Voucher selectedVoucher;

    @Override
    public void initData(Voucher data) {
        this.selectedVoucher = data;
        voucherCodeField.setText(data.getVoucherCode());
        voucherPercentageField.setText(String.valueOf(data.getVoucherPercentage()));
        voucherQuantityField.setText(String.valueOf(data.getVoucherQuantity()));
        startDatePicker.setValue(data.getStartDate());
        endDatePicker.setValue(data.getEndDate());
        statusComboBox.setValue(data.getStatus());
    }

    @FXML
    public void handleSave() throws SQLException {
        String voucherCode = voucherCodeField.getText();
        double voucherPercentage = Double.parseDouble(voucherPercentageField.getText());
        int voucherQuantity = Integer.parseInt(voucherQuantityField.getText());
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String status = (String) statusComboBox.getValue();

        String updateQuery = "UPDATE vouchers SET voucher_code = ?, voucher_percentage = ?, voucher_quantity = ?, start_date = ?, end_date = ?, status = ? WHERE voucher_id = ?";
        Connection connection = MySQLConnection.connect();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, voucherCode);
            preparedStatement.setDouble(2, voucherPercentage);
            preparedStatement.setInt(3, voucherQuantity);
            preparedStatement.setString(4, startDate.toString());
            preparedStatement.setString(5, endDate.toString());
            preparedStatement.setString(6, status);
            preparedStatement.setInt(7, selectedVoucher.getVoucherId());
            preparedStatement.executeUpdate();
            Stage stage = (Stage) voucherCodeField.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(null);
        }
    }



}
