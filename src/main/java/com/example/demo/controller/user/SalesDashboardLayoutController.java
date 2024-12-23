package com.example.demo.controller.user;

import com.example.demo.Utils.Config;
import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.model.ProductSearch;
import com.example.demo.model.Shift;
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
    private TableColumn<ProductSearch, Double> colGia;
    @FXML
    private TableColumn<ProductSearch, Integer> colSoLuong;
    @FXML
    private TableColumn<ProductSearch, Double> colChietKhau;
    @FXML
    private TableColumn<ProductSearch, Double> colThanhTien;

    @FXML
    private TextField salesDateField;

    @FXML
    private void onSearch(ActionEvent event) throws SQLException, FileNotFoundException {
        String barcode = searchField.getText();

        if (barcode != null && !barcode.isEmpty()) {
            ProductSearch product = searchProductByBarcode(barcode);
            if (product != null) {
                int stt = productList.size() + 1;
                product.setStt(stt);
                productList.add(product);
                updateTableView();
            } else {
                Modal.showAlert("Sản phẩm không tồn tại");
            }
            searchField.clear();
        }
    }

    private ProductSearch searchProductByBarcode(String barcode) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/plants", "root", "");
            String query = """
            SELECT p.name, p.price, p.description, c.category, v.quantity, v.code, v.discount_id, d.discount_percentage, i.image
            FROM variants v
            JOIN products p ON v.product_id = p.product_id
            JOIN categories c ON p.category_id = c.category_id
            LEFT JOIN discounts d ON v.discount_id = d.discount_id
            LEFT JOIN images i ON p.product_id = i.product_id
            WHERE v.code = ?
        """;

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, barcode);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String loai = resultSet.getString("description");
                String tenSanPham = resultSet.getString("name");
                double gia = resultSet.getDouble("price");
                int soLuong = 1;
                double discountPercentage = resultSet.getDouble("discount_percentage");
                double thanhTien = gia * soLuong * (1 - discountPercentage / 100);
                String imageUrl = resultSet.getString("image");
                return new ProductSearch(1, tenSanPham,imageUrl, loai, gia, soLuong, discountPercentage, thanhTien);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }

        return null;
    }

    private void updateTableView() {
        colStt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getStt()));
        colTenSanPham.setCellValueFactory(cellData -> cellData.getValue().tenSanPhamProperty());
        colLoai.setCellValueFactory(cellData -> cellData.getValue().loaiProperty());
        colGia.setCellValueFactory(cellData -> cellData.getValue().giaProperty().asObject());
        colSoLuong.setCellValueFactory(cellData -> cellData.getValue().soLuongProperty().asObject());
        colChietKhau.setCellValueFactory(cellData -> cellData.getValue().chietKhauProperty().asObject());
        colThanhTien.setCellValueFactory(cellData -> cellData.getValue().thanhTienProperty().asObject());
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
        }else {
            Modal.showAlert("Chưa bắt đầu ngày");
        }

    }

    public void onStartTheDay(ActionEvent actionEvent) throws IOException {
        StartTheDayController startTheDayController = new StartTheDayController();
        if (startTheDayController.check_day()) {
            Modal.showModal("/com/example/demo/controller/auth/view/user/starttheday/starttheday.fxml", "Bắt đầu ngày", null);
        } else {
            Modal.showAlert("Đã có người check in");
        }
    }

    public void onEndDay(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/endday/endday.fxml", "Kết thúc ngày", this::Countshift);
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
            saleshiftnumber.setText(String.valueOf(count));
        }else{
            saleshiftnumber.setText("0");
        }
    }


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

}



