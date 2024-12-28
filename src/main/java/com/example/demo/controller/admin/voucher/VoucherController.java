package com.example.demo.controller.admin.voucher;

import com.example.demo.Utils.Modal;
import com.example.demo.config.MySQLConnection;
import com.example.demo.model.Voucher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.example.demo.Utils.Modal.*;

public class VoucherController {
    @FXML
    private TableColumn<Voucher, String> actionColumn;
    @FXML
    private TableView<Voucher> voucherTable;

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
        voucherCodeColumn.setCellValueFactory(new PropertyValueFactory<>("voucherCode"));
        voucherPercentageColumn.setCellValueFactory(new PropertyValueFactory<>("voucherPercentage"));
        voucherQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("voucherQuantity"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        actionColumn.setCellFactory(new Callback<TableColumn<Voucher, String>, TableCell<Voucher, String>>() {
            @Override
            public TableCell<Voucher, String> call(TableColumn<Voucher, String> param) {
                return new TableCell<Voucher, String>() {
                    private final Button deleteButton = new Button("Xoá");
                    private final Button editButton = new Button("Sửa");

                    {
                        deleteButton.getStyleClass().add("delete-button");
                        editButton.getStyleClass().add("edit-button");
                        deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
                        editButton.setOnAction(event -> {
                            try {
                                handleEdit(getTableRow().getItem());
                            } catch (IOException | SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hBox = new HBox(3);
                            hBox.setAlignment(Pos.CENTER);
                            editButton.setMaxWidth(Double.MAX_VALUE);
                            deleteButton.setMaxWidth(Double.MAX_VALUE);
                            hBox.getChildren().addAll(editButton, deleteButton);
                            setGraphic(hBox);
                        }
                    }
                };
            }
        });
        loadData();
    }


    @FXML
    void loadData() {
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
        showModal("/com/example/demo/controller/auth/view/admin/voucher/upload-view.fxml", "", this::loadData);
    }


    private void handleDelete(Voucher voucher) {
        Modal.showAlert(null, "Bạn có chắc chắn muốn xoá Voucher này không?", Alert.AlertType.CONFIRMATION, () -> {
            String query = "DELETE FROM vouchers WHERE voucher_id = " + voucher.getVoucherId();
            Connection connection = MySQLConnection.connect();
            try {
                if (connection == null || connection.isClosed()) {
                    System.err.println("Không thể kết nối tới cơ sở dữ liệu.");
                    return;
                }
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(query);
                    voucherList.remove(voucher);
                    loadData();
                }

            } catch (SQLException e) {
                showAlert(null);
                e.printStackTrace();
            }
        }, null);
    }

    private void handleEdit(Voucher voucher) throws IOException, SQLException {
        showModalWithData("/com/example/demo/controller/auth/view/admin/voucher/update-view.fxml", "Sửa phiếu giảm giá", voucher, this::loadData);
    }

}
