package com.example.demo.controller.user;

import com.example.demo.Utils.Config;
import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.model.ProductSearch;
import com.example.demo.model.ProductView;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

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

    @FXML
    private TextField displayField;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> suggestionListView;

    @FXML
    private TableView<ProductSearch> tableView; // TableView hiển thị kết quả tìm kiếm
    @FXML
    private TableColumn<ProductSearch, String> productNameColumn; // Cột tên sản phẩm
    @FXML
    private TableColumn<ProductSearch, String> productCategoryColumn; // Cột kích thước sản phẩm
    @FXML
    private TableColumn<ProductSearch, Double> productPriceColumn; // Cột giá sản phẩm
    @FXML
    private TableColumn<ProductSearch, Integer> productQuantityColumn; // Cột số lượng sản phẩm
    @FXML
    private TableColumn<ProductSearch, String> productDiscountColumn;
    @FXML
    private Connection connection;
    @FXML
    private TableColumn<ProductSearch, Integer> sttProperty;
    @FXML
    private TableColumn<ProductSearch, String> totalAmountProperty;
    @FXML
    private TableColumn<ProductSearch, String> imageColumn;
    @FXML
    private StackPane rootPane; // StackPane chứa các thành phần của UI
    @FXML
    private TabPane tabPane;


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
        Modal.showModal("/com/example/demo/controller/auth/view/user/closeshift/closeshift.fxml", "Kết thúc ca", null);

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
    private void updateSearchSuggestions(String searchText) {
        String query = "SELECT p.name FROM products p WHERE p.name LIKE ? LIMIT 10";  // Tìm sản phẩm phù hợp

        ObservableList<String> suggestions = FXCollections.observableArrayList();

        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/plants", "root", "");
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + searchText + "%");  // Tìm sản phẩm chứa từ khóa
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                suggestions.add(rs.getString("name"));  // Thêm tên sản phẩm vào danh sách gợi ý
            }

            if (suggestions.isEmpty()) {
                suggestionListView.setVisible(false);  // Ẩn ListView nếu không có gợi ý
            } else {
//                tableView.setVisible(false);
                suggestionListView.setVisible(true);
            }

            suggestionListView.setItems(suggestions);  // Hiển thị danh sách gợi ý
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addProductToTable(String productName) {
        String query = """
            SELECT
                p.product_id,
                p.name,
                i.image,
                c.category,
                p.price,
                v.quantity,
                d.discount_percentage
            FROM products p
            LEFT JOIN categories c ON p.category_id = c.category_id
            LEFT JOIN images i ON p.product_id = i.product_id
            LEFT JOIN variants v ON p.product_id = v.product_id
            LEFT JOIN discounts d ON v.discount_id = d.discount_id
            WHERE p.name = ?
            AND i.image_id IN (
                SELECT MIN(image_id)
                FROM images
                WHERE product_id = p.product_id
                GROUP BY product_id
            )
        """;

        ObservableList<ProductSearch> products = FXCollections.observableArrayList();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, productName);  // Truy vấn sản phẩm theo tên đã chọn
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String image = rs.getString("image");
                String category = rs.getString("category");
                BigDecimal price = rs.getBigDecimal("price");
                int quantity = rs.getInt("quantity");
                double totalAmount = price.doubleValue() * quantity;

                // Tạo đối tượng ProductSearch và thêm vào TableView
                ProductSearch product = new ProductSearch(name, quantity, price.doubleValue(), category, image, totalAmount);
                products.add(product);
            }

            tableView.setItems(products);  // Hiển thị sản phẩm trong TableView
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() throws IOException {
        salesDateField.setText(Config.getCurrentDate());

        suggestionListView.setVisible(false);


        if (suggestionListView != null) {
            suggestionListView.getSelectionModel().selectFirst();
        }

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                updateSearchSuggestions(newValue);  // Cập nhật gợi ý tìm kiếm
            } else {
                suggestionListView.setItems(FXCollections.observableArrayList());
                suggestionListView.setVisible(false);
            }
        });

        // Lắng nghe sự kiện khi người dùng chọn sản phẩm trong ListView
        suggestionListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                addProductToTable(newValue);  // Thêm sản phẩm vào TableView khi chọn
                suggestionListView.setItems(FXCollections.observableArrayList()); // Xóa gợi ý khi đã chọn sản phẩm
                suggestionListView.setVisible(false);
            }
        });
    }




//    //        try {
////            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/plants", "root", "");
////            sttProperty.setCellValueFactory(cellData -> cellData.getValue().sttProperty().asObject());
////            productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
////            productPriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
////            productQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().stockQuantityProperty().asObject());
////            productCategoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
//////            productDiscountColumn.setCellValueFactory(cellData -> cellData.getValue().discountStatusProperty());
////            totalAmountProperty.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty().asString());
////            imageColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getProductImage()));
////            imageColumn.setCellFactory(col -> new TableCell<ProductSearch, String>() {
////                private final ImageView imageView = new ImageView();
////
////                @Override
////                protected void updateItem(String item, boolean empty) {
////                    super.updateItem(item, empty);
////                    if (empty || item == null || item.isEmpty()) {
////                        setGraphic(null);
////                    } else {
////                        try {
////                            Image image = new Image("file:" + item);
////                            imageView.setImage(image);
////                            imageView.setFitHeight(50);
////                            imageView.setFitWidth(50);
////                            setGraphic(imageView);
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                            imageView.setImage(new Image("file:default-image.png"));
////                            setGraphic(imageView);
////                        }
////                    }
////                }
////            });
////
////            searchButton.setOnAction(event -> updateTableData());
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }


//    private void updateTableData() {
//        String searchText = searchField.getText().trim();
//
//        if (searchText.isEmpty()) {
//            // Nếu ô tìm kiếm rỗng, không tìm gì cả
//            tableView.setItems(FXCollections.observableArrayList());
//            return;
//        }
//
//        // Truy vấn SQL với điều kiện phân biệt chữ hoa/chữ thường và tìm chính xác
//        String searchQuery = """
//                SELECT
//                    p.product_id,
//                    p.name,
//                    i.image,
//                    c.category,
//                    p.price,
//                    v.quantity,
//                    d.discount_percentage
//                FROM products p
//                LEFT JOIN categories c ON p.category_id = c.category_id
//                LEFT JOIN images i ON p.product_id = i.product_id
//                LEFT JOIN variants v ON p.product_id = v.product_id
//                LEFT JOIN discounts d ON v.discount_id = d.discount_id
//                WHERE BINARY p.name = ?
//                AND i.image_id IN (
//                    SELECT MIN(image_id)
//                    FROM images
//                    WHERE product_id = p.product_id
//                    GROUP BY product_id
//                )
//            """;
//        ObservableList<ProductSearch> products = FXCollections.observableArrayList();
//
//        try (PreparedStatement statement = connection.prepareStatement(searchQuery)) {
//            // Gán tham số tìm kiếm
//            statement.setString(1, searchText); // Chỉ gán trực tiếp từ khóa
//
//            ResultSet resultSet = statement.executeQuery();
//            int stt = 1;
//
//            while (resultSet.next()) {
//                String name = resultSet.getString("name");
//                String image = resultSet.getString("image");
//                String category = resultSet.getString("category");
//                BigDecimal price = resultSet.getBigDecimal("price");
//                int quantity = resultSet.getInt("quantity");
//                double totalAmount = price.doubleValue() * quantity;
//
//                // Tạo đối tượng ProductSearch
//                ProductSearch product = new ProductSearch(name, quantity, price.doubleValue(), category, image, totalAmount);
//                product.setStt(stt++);
//                product.setTotalAmount(totalAmount);
//                products.add(product);
//            }
//            tableView.setItems(products);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }



}


