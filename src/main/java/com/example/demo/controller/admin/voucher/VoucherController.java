package com.example.demo.controller.admin.voucher;

import com.example.demo.config.MySQLConnection;
import com.example.demo.model.Voucher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.example.demo.Utils.Modal.showModal;

public class VoucherController {

    @FXML
    private TableView<Voucher> voucherTable;

    @FXML
    private TableColumn<Voucher, Integer> voucherIdColumn;

    @FXML
    private TableColumn<Voucher, String> voucherCodeColumn;

    @FXML
    private TableColumn<Voucher, Double> voucherPercentageColumn;

    @FXML
    private TableColumn<Voucher, Integer> voucherQuantityColumn;

    @FXML
    private TableColumn<Voucher, String> startDateColumn;

    @FXML
    private TableColumn<Voucher, String> endDateColumn;

    @FXML
    private TableColumn<Voucher, String> statusColumn;

    private final ObservableList<Voucher> voucherList = FXCollections.observableArrayList();

    public void initialize() {
        voucherIdColumn.setCellValueFactory(new PropertyValueFactory<>("voucherId"));
        voucherCodeColumn.setCellValueFactory(new PropertyValueFactory<>("voucherCode"));
        voucherPercentageColumn.setCellValueFactory(new PropertyValueFactory<>("voucherPercentage"));
        voucherQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("voucherQuantity"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadData();
    }
    @FXML
    private void loadData() {
        String query = "SELECT * FROM vouchers";
        Connection connection = MySQLConnection.connect();
        try {
            assert connection != null;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                voucherList.clear();
                while (resultSet.next()) {
                    int voucherId = resultSet.getInt("voucher_id");
                    String voucherCode = resultSet.getString("voucher_code");
                    double voucherPercentage = resultSet.getDouble("voucher_percentage");
                    int voucherQuantity = resultSet.getInt("voucher_quantity");
                    LocalDate startDate = LocalDate.parse(resultSet.getString("start_date"));
                    LocalDate endDate = LocalDate.parse(resultSet.getString("end_date"));
                    String status = resultSet.getString("status");

                    Voucher voucher = new Voucher(voucherId, voucherCode, voucherPercentage, voucherQuantity, startDate, endDate, status);
                    voucherList.add(voucher);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while loading voucher data: " + e.getMessage());
            e.printStackTrace();
        }

        voucherTable.setItems(voucherList);
    }

    @FXML
    public void uploadOnClick(ActionEvent actionEvent) throws IOException {
        showModal("/com/example/demo/controller/auth/view/admin/voucher/upload-view.fxml", "Đăng phiếu giảm giá");
    }
}
