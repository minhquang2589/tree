package com.example.demo.controller.user;

import com.example.demo.Utils.Config;
import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.config.MySQLConnection;
import com.example.demo.model.ProductSearch;
import com.example.demo.model.Shift;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.HashMap;

import static com.example.demo.Utils.Config.calculateCartTotal;
import static com.example.demo.Utils.Config.formatCurrencyVND;
import static com.example.demo.Utils.Modal.showAlert;
import static com.example.demo.config.button.ButtonHandler.handleNavigator;

import com.example.demo.controller.user.starttheday.StartTheDayController;
import javafx.scene.text.Text;

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
    private TextField displayField;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<ProductSearch> productTable;
    @FXML
    private ObservableList<ProductSearch> productList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<ProductSearch, Integer> colStt;
    @FXML
    private TableColumn<ProductSearch, String> colTenSanPham;
    @FXML
    private TableColumn<ProductSearch, String> colImage;
    @FXML
    private TableColumn<ProductSearch, String> colLoai;

    @FXML
    private TableColumn<ProductSearch, String> colSize;
    @FXML
    private TableColumn<ProductSearch, Double> colGia;
    @FXML
    private TableColumn<ProductSearch, Integer> colSoLuong;
    @FXML
    private TableColumn<ProductSearch, Double> colChietKhau;
    @FXML
    private TableColumn<ProductSearch, Double> colThanhTien;

    @FXML
    private Text treoPhieuText;

    @FXML
    private TextField salesDateField;

    @FXML
    private TextField saleshiftnumber;

    private final HashMap<String, ObservableList<ProductSearch>> pendingOrders = new HashMap<>();
    private int orderCounter = 1;

    public void initialize() throws IOException {
        salesDateField.setText(Config.getCurrentDate());
        Countshift();
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    onSearch(null);
                } catch (SQLException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @FXML
    public void onPayment(ActionEvent actionEvent) throws IOException {
        if (productList.isEmpty()) {
            Modal.showAlert("Giỏ hàng hiện tại đang trống. Hãy thêm sản phẩm để thanh thoán!");
        } else {
            Modal.showModalWithData("/com/example/demo/controller/auth/view/user/paymentprocessing/paymentProcessing.fxml", "Chọn các hình thức thanh toán bằng cách bấm vào ô tương ứng.", productList, () -> {
                System.out.println("payment Processing call back ");
            });
        }
    }

    @FXML
    private void onHoldOrder(ActionEvent event) {
        if (!productList.isEmpty()) {
            String newOrderId = "Order-" + orderCounter++;
            pendingOrders.put(newOrderId, FXCollections.observableArrayList(productList));
            showAlert("Thông báo", "Đã treo phiếu: " + newOrderId, Alert.AlertType.INFORMATION, null, null);
            productList.clear();
            productTable.setItems(productList);
            treoPhieuText.setText("Phiếu treo: " + pendingOrders.size());
        } else {
            showAlert("Thông báo", "Không có sản phẩm để treo phiếu!", Alert.AlertType.WARNING, null, null);
        }
    }

    @FXML
    private void onRecallOrder(ActionEvent event) {
        if (pendingOrders.isEmpty()) {
            showAlert("Thông báo", "Không có phiếu nào để gọi!", Alert.AlertType.WARNING, null, null);
            return;
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, pendingOrders.keySet());
        dialog.setTitle("Gọi phiếu");
        dialog.setHeaderText("Chọn phiếu để gọi");
        dialog.setContentText("Danh sách phiếu:");
        dialog.showAndWait().ifPresent(orderId -> {
            if (!productList.isEmpty()) {
                String currentOrderId = "Order-" + orderCounter++;
                pendingOrders.put(currentOrderId, FXCollections.observableArrayList(productList));
                productList.clear();
            }
            ObservableList<ProductSearch> order = pendingOrders.remove(orderId);
            if (order != null) {
                productList.addAll(order);
                productTable.setItems(productList);
                showAlert("Thông báo", "Đã gọi phiếu: " + orderId, Alert.AlertType.INFORMATION, null, null);
            }
            treoPhieuText.setText("Phiếu treo: " + pendingOrders.size());
        });
    }


    @FXML
    private void onSearch(ActionEvent event) throws SQLException, FileNotFoundException {
        String barcode = searchField.getText();

        if (barcode != null && !barcode.isEmpty()) {
            ProductSearch newProduct = searchProductByBarcode(barcode);
            if (newProduct != null) {
                ProductSearch existingProduct = checkProductIfExits(newProduct.variantIdProperty());
                if (existingProduct != null) {
                    int updatedQuantity = existingProduct.getSoLuong() + newProduct.getSoLuong();
                    existingProduct.setSoLuong(updatedQuantity);

                    double updatedPrice = existingProduct.getGia() * updatedQuantity * (1 - existingProduct.getChietKhau() / 100);
                    existingProduct.setThanhTien(updatedPrice);

                    updateTableView();
                } else {
                    int stt = productList.size() + 1;
                    newProduct.setStt(stt);
                    productList.add(newProduct);
                    updateTableView();
                }
            } else {
                showAlert("Sản phẩm không tồn tại");
            }
            searchField.clear();
        }
    }


    private ProductSearch searchProductByBarcode(String barcode) throws SQLException {
        Connection connection = MySQLConnection.connect();

        String query = """
                    SELECT p.name, v.variant_id AS variant_id, p.description, c.category, v.quantity, v.price, v.code, v.discount_id, d.*, i.image, s.size
                    FROM variants v
                    JOIN products p ON v.product_id = p.product_id
                    JOIN categories c ON p.category_id = c.category_id
                    LEFT JOIN discounts d ON v.discount_id = d.discount_id
                        AND d.discount_percentage > 0
                        AND d.discount_quantity > 0
                        AND d.discount_remaining > 0
                        AND d.end_date >= CURDATE()
                    LEFT JOIN images i ON p.product_id = i.product_id
                    JOIN sizes s ON v.size_id = s.size_id
                    WHERE v.code = ?
                """;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, barcode);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String loai = resultSet.getString("category");
                int variant_id = resultSet.getInt("variant_id");
                String size = resultSet.getString("size");
                String tenSanPham = resultSet.getString("name");
                double gia = resultSet.getDouble("price");
                int soLuong = 1;
                double discountPercentage = resultSet.getDouble("discount_percentage");
                double thanhTien = gia * soLuong * (1 - discountPercentage / 100);
                String imageUrl = resultSet.getString("image");
                return new ProductSearch(1, tenSanPham, imageUrl, loai, gia, soLuong, discountPercentage, thanhTien, size, variant_id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void updateTableView() {
        colStt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getStt()));
        colTenSanPham.setCellValueFactory(cellData -> cellData.getValue().tenSanPhamProperty());
        colLoai.setCellValueFactory(cellData -> cellData.getValue().loaiProperty());
        colSize.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
        colGia.setCellValueFactory(cellData -> cellData.getValue().giaProperty().asObject());
        colGia.setCellFactory(column -> new TableCell<ProductSearch, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatCurrencyVND(item));
                }
            }
        });

        colSoLuong.setCellValueFactory(cellData -> cellData.getValue().soLuongProperty().asObject());
        colChietKhau.setCellValueFactory(cellData -> cellData.getValue().chietKhauProperty().asObject());
        colThanhTien.setCellValueFactory(cellData -> cellData.getValue().thanhTienProperty().asObject());
        colThanhTien.setCellFactory(column -> new TableCell<ProductSearch, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatCurrencyVND(item));
                }
            }
        });

        colImage.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getImage()));
        colImage.setCellFactory(col -> new TableCell<ProductSearch, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Image image = new Image("file:" + item);
                    imageView.setImage(image);
                    imageView.setFitHeight(80);
                    imageView.setFitWidth(160);
                    setGraphic(imageView);
                }
            }
        });

        if (productTable.getColumns().stream().noneMatch(col -> col.getText().isEmpty())) {
            TableColumn<ProductSearch, Void> colDelete = new TableColumn<>("");
            colDelete.setCellFactory(col -> new TableCell<ProductSearch, Void>() {
                private final Button deleteButton = new Button("Xóa");

                {
                    deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
                    deleteButton.setOnAction(event -> {
                        ProductSearch product = getTableView().getItems().get(getIndex());
                        if (!productList.isEmpty() && product != null) {
                            showAlert(
                                    "Xác nhận xoá",
                                    "Bạn có chắc chắn muốn xoá sản phẩm này khỏi giỏ hàng không?",
                                    Alert.AlertType.CONFIRMATION,
                                    () -> {
                                        productList.remove(product);
                                        getTableView().refresh();
                                    }, null
                            );
                        } else {
                            showAlert("Không thể xoá sản phẩm. vui lòng thử lại sau!");
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            });
            productTable.getColumns().add(colDelete);
        }
        productTable.setItems(productList);
    }


    public void onSales(ActionEvent actionEvent) throws IOException {
        StartTheDayController startTheDayController = new StartTheDayController();
        if (!startTheDayController.check_day()) {
            sl_sales.setVisible(false);
            sl_sales2.setVisible(false);
            sl_CancelTransaction.setVisible(true);
            sl_Payment.setVisible(true);
        } else {
            showAlert(
                    "Thông báo",
                    "Chưa bắt đầu ngày",
                    Alert.AlertType.INFORMATION,
                    null, null
            );
        }
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

    public void onCheckPrice(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/checkprice/checkprice.fxml", "Kiểm tra giá sản phẩm", null);
    }

    public void onOut(ActionEvent actionEvent) {
        PreferencesUtils.clearUser();
        handleNavigator(actionEvent, "/com/example/demo/controller/auth/login-view.fxml", true);
    }

    public void onCloseshift(ActionEvent actionEvent) throws IOException {
        StartTheDayController startTheDayController = new StartTheDayController();
        if (!startTheDayController.check_day()) {
            Modal.showModal("/com/example/demo/controller/auth/view/user/closeshift/closeshift.fxml", "Kết thúc ca", this::Countshift);
        } else {
            showAlert("Chưa bắt đầu ngày");
        }

    }

    public void onStartTheDay(ActionEvent actionEvent) throws IOException {
        StartTheDayController startTheDayController = new StartTheDayController();
        if (startTheDayController.check_day()) {
            Modal.showModal("/com/example/demo/controller/auth/view/user/starttheday/starttheday.fxml", "Bắt đầu ngày", null);
        } else {
            showAlert("Đã có người check in");
        }
    }

    public void onEndDay(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/endday/endday.fxml", "Kết thúc ngày", this::Countshift);
    }

    public void onExitApplication(ActionEvent event) {
        showAlert(
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
    public void Countshift() {
        List<Shift> shifts = PreferencesUtils.getShiftList();
        int count = shifts.size();
        if (count > 0) {
            saleshiftnumber.setText(String.valueOf(count));
        } else {
            saleshiftnumber.setText("0");
        }
    }


    private ProductSearch checkProductIfExits(IntegerProperty variantId) {
        for (ProductSearch product : productList) {
            if (product.variantIdProperty().get() == variantId.get()) {
                return product;
            }
        }
        return null;
    }


}



