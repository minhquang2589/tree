package com.example.demo.controller.user;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.prefs.BackingStoreException;

import com.example.demo.Utils.Config;

import com.example.demo.Utils.Modal;

import static com.example.demo.Utils.Config.*;
import static com.example.demo.Utils.Modal.showAlert;

import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.config.MySQLConnection;

import static com.example.demo.config.button.ButtonHandler.handleNavigator;
import static com.example.demo.controller.admin.voucher.VoucherController.applyVoucher;
import static com.example.demo.controller.user.OrderController.saveOrders;

import com.example.demo.controller.user.starttheday.StartTheDayController;
import com.example.demo.model.ProductSearch;
import com.example.demo.model.Shift;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

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
    public static ObservableList<ProductSearch> productList = FXCollections.observableArrayList();

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

    private Map<String, List<ProductSearch>> pendingOrders = new HashMap<>();

    private String orderPendingCode = null;

    public void initialize() throws IOException, BackingStoreException {
        countPeddingOrder();
        applyVoucher = null;
        updateDate();
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    onSearch(null);
                } catch (SQLException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        productList.addListener((ListChangeListener<ProductSearch>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved() || change.wasUpdated()) {
                    updateTableView();
                }
            }
        });

        colSoLuong.setCellValueFactory(cellData -> cellData.getValue().soLuongProperty().asObject());
        colSoLuong.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colSoLuong.setCellFactory(column -> new TableCell<>() {
            private final Button increaseButton = new Button("+");
            private final Button decreaseButton = new Button("-");
            private final Label quantityLabel = new Label();
            private final HBox hbox = new HBox(decreaseButton, quantityLabel, increaseButton);

            {
                hbox.setSpacing(15);
                hbox.setAlignment(Pos.CENTER);
                increaseButton.setOnAction(event -> {
                    ProductSearch product = getTableView().getItems().get(getIndex());
                    int currentQuantity = product.getSoLuong();
                    int maxQuantity = Integer.parseInt(product.getProductQty());

                    if (currentQuantity < maxQuantity) {
                        product.setSoLuong(currentQuantity + 1);
                        double updatedPrice = product.getGia() * product.getSoLuong() * (1 - product.getChietKhau() / 100);
                        product.setThanhTien(updatedPrice);
                        quantityLabel.setText(String.valueOf(product.getSoLuong()));
                        productTable.refresh();
                    } else {
                        showAlert("Số lượng nhập vào lớn hơn số lượng tồn kho hiện tại!");
                    }
                });

                decreaseButton.setOnAction(event -> {
                    ProductSearch product = getTableView().getItems().get(getIndex());
                    int currentQuantity = product.getSoLuong();
                    if (currentQuantity > 1) {
                        product.setSoLuong(currentQuantity - 1);
                        double updatedPrice = product.getGia() * product.getSoLuong() * (1 - product.getChietKhau() / 100);
                        product.setThanhTien(updatedPrice);
                        quantityLabel.setText(String.valueOf(product.getSoLuong()));
                        productTable.refresh();
                    }
                });
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    ProductSearch product = getTableView().getItems().get(getIndex());
                    quantityLabel.setText(String.valueOf(product.getSoLuong()));
                    setGraphic(hbox);
                }
            }
        });


    }

    @FXML
    private void onHoldOrder(ActionEvent event) throws BackingStoreException {
        if (!productList.isEmpty()) {
            String newOrderId = "Order_" + pendingOrders.size();
            Map<String, List<ProductSearch>> orderMap = new HashMap<>();
            if (orderPendingCode != null) {
                showAlert("Thông báo", "Bạn đang gọi phiếu  " + orderPendingCode + " bạn có muốn treo lại phiếu " + orderPendingCode + " không?", Alert.AlertType.CONFIRMATION,
                        () -> {
                            orderMap.put(orderPendingCode, new ArrayList<>(productList));
                            saveOrders(orderMap);
                            productList.clear();
                        }, null);
            } else {
                orderMap.put(newOrderId, new ArrayList<>(productList));
                saveOrders(orderMap);
                showAlert("Đã treo phiếu:  " + newOrderId);
                orderPendingCode = null;
                productList.clear();
                countPeddingOrder();
            }

        } else {
            showAlert("Thông báo", "Không có sản phẩm để treo phiếu!", Alert.AlertType.WARNING, null, null);
        }
    }


    @FXML
    private void onRecallOrder(ActionEvent event) throws BackingStoreException {
        pendingOrders = OrderController.loadOrders();

        if (pendingOrders.isEmpty()) {
            showAlert("Thông báo", "Không có phiếu nào để gọi!", Alert.AlertType.WARNING, null, null);
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, pendingOrders.keySet());
        dialog.setTitle("Gọi phiếu");
        dialog.setHeaderText("Chọn phiếu để gọi");
        dialog.setContentText("Danh sách phiếu:");
        dialog.setResizable(true);
        dialog.getDialogPane().setPrefSize(350, 200);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String orderId = result.get();
            List<ProductSearch> order = pendingOrders.get(orderId);

            if (order != null) {
                productList.clear();
                productList.addAll(order);
                productTable.setItems(productList);
                treoPhieuText.setText("Phiếu treo: " + pendingOrders.size());
                orderPendingCode = orderId;
            }
        }
    }

    private ProductSearch searchProductByBarcode(String barcode) throws SQLException {
        Connection connection = MySQLConnection.connect();

        String query = """
                    SELECT  p.description AS pro_des,  p.is_new AS isNew,  p.category_id AS pCateId,  p.product_id, p.name, v.variant_id AS variant_id,v.size_id AS vSizeId, p.description, c.category, v.quantity AS vQty, v.price,d.discount_remaining, v.code, v.discount_id AS product_discount_id, d.*, i.image,i.image_id, s.size
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
                String discountId = resultSet.getString("product_discount_id");
                String productId = resultSet.getString("product_id");
                String code = resultSet.getString("code");
                String des = resultSet.getString("pro_des");
                String sizeId = resultSet.getString("vSizeId");
                String cateId = resultSet.getString("pCateId");
                String imageId = resultSet.getString("image_id");
                boolean isNew = resultSet.getInt("isNew") == 1;
                String vQty = resultSet.getString("vQty");
                String discountRemaining = resultSet.getString("discount_remaining");
                return new ProductSearch(1, tenSanPham, imageUrl, loai, gia, soLuong, discountPercentage, thanhTien, size, variant_id, discountId, productId, code, des, sizeId, cateId, imageId, isNew, vQty, discountRemaining);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateTableView() {
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
        colChietKhau.setCellFactory(column -> new TableCell<ProductSearch, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item + " %");
                }
            }
        });
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
                    imageView.setFitHeight(120);
                    imageView.setFitWidth(180);
                    imageView.setPreserveRatio(true);
                    imageView.setStyle("-fx-border-color: gray; -fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-width: 1px; -fx-effect: innershadow( gaussian , rgba(0,0,0,0.1), 10, 0, 0, 0 );");
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
                                        if(orderPendingCode != null){
                                            try {
                                                OrderController.removeOrder(orderPendingCode);
                                                countPeddingOrder();
                                            } catch (BackingStoreException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
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
            showAlert("Chưa bắt đầu ngày");

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
            Modal.showModal("/com/example/demo/controller/auth/view/user/starttheday/starttheday.fxml", "Bắt đầu ngày", this::checkDay);
        } else {
            showAlert("Đã có người check in");
        }
    }

    public void onEndDay(ActionEvent actionEvent) throws IOException {
        StartTheDayController startTheDayController = new StartTheDayController();
        if (!startTheDayController.check_day()) {
            Modal.showModal("/com/example/demo/controller/auth/view/user/endday/endday.fxml", "Kết thúc ngày", this::updateDate);
        } else {
            Modal.showAlert("Chưa bắt đầu ngày");
        }
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

    public void checkDay() {
        StartTheDayController startTheDayController = new StartTheDayController();
        try {
            if (!startTheDayController.check_day() && !startTheDayController.check_day_end()) {
                salesDateField.setText(Config.getCurrentDate());
            } else {
                salesDateField.setText("đã đóng");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateDate() {
        Countshift();
        checkDay();
    }


    private ProductSearch checkProductIfExits(IntegerProperty variantId) {
        for (ProductSearch product : productList) {
            if (product.variantIdProperty().get() == variantId.get()) {
                return product;
            }
        }
        return null;
    }

    @FXML
    public void onPayment(ActionEvent actionEvent) throws IOException, SQLException {
        if (productList.isEmpty()) {
            Modal.showAlert("Giỏ hàng hiện tại đang trống. Hãy thêm sản phẩm để thanh thoán!");
        } else {
            Modal.showModalWithData("/com/example/demo/controller/auth/view/user/paymentprocessing/paymentProcessing.fxml", "Chọn các hình thức thanh toán bằng cách bấm vào ô tương ứng.", productList, () -> {
                applyVoucher = null;
                updateTableView();
            });
        }
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

    public void cancelHandle(ActionEvent actionEvent) {
        if (productList.isEmpty()) {
            showAlert("Không có giao dịch nào đang thực hiện!");
            return;
        }
        showAlert(
                "Huỷ giao dịch",
                "Bạn có chắc chắn muốn huỷ" + " giao dịch " + (orderPendingCode != null ? (orderPendingCode) : "") + " không?",
                Alert.AlertType.CONFIRMATION,
                () -> {
                    if (orderPendingCode != null) {
                        try {
                            OrderController.removeOrder(orderPendingCode);
                            orderPendingCode = null;
                            countPeddingOrder();
                        } catch (BackingStoreException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    productList.clear();
                }, null
        );

    }

    private void countPeddingOrder() throws BackingStoreException {
        pendingOrders = OrderController.loadOrders();
        treoPhieuText.setText("Phiếu đang treo: " + pendingOrders.size());
    }


}



