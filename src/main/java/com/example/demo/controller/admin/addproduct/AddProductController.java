package com.example.demo.controller.admin.addproduct;
import com.example.demo.Utils.Config;
import com.example.demo.config.MySQLConnection;
import com.example.demo.model.ProductSearch;
import com.example.demo.model.ProductView;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Utils.Config.formatCurrencyVND;

public class AddProductController {
    @FXML
    public ListView imageListView;
    @FXML
    private TableColumn<ProductView, String> imageColumn;
    @FXML
    private TableColumn<ProductView, String> sttColumn;
    @FXML
    private TableView<ProductView> productTableView;

    @FXML
    private TableColumn<ProductView, String> productNameColumn;

    @FXML
    private TableColumn<ProductView, String> sizeColumn;

    @FXML
    private TableColumn<ProductView, Integer> quantityColumn;

    @FXML
    private TableColumn<ProductView, Double> priceColumn;

    @FXML
    private TableColumn<ProductView, String> dateColumn;

    @FXML
    private TableColumn<ProductView, String> discountColumn;

    @FXML
    private TableColumn<ProductView, String> isNewColumn;

    @FXML
    private TableColumn<ProductView, String> descriptionColumn;

    private ObservableList<ProductView> productList;
    private final List<String> images = new ArrayList<>();

    public void initialize() {
        productList = FXCollections.observableArrayList();

        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        sizeColumn.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().stockQuantityProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

        priceColumn.setCellFactory(column -> new TableCell<ProductView, Double>() {
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
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().updatedDateProperty());
        discountColumn.setCellValueFactory(cellData -> cellData.getValue().discountStatusProperty());
        isNewColumn.setCellValueFactory(cellData -> cellData.getValue().isNewProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        sttColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(productTableView.getItems().indexOf(cellData.getValue()) + 1).asString()
        );
        imageColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getProductImage()));
        imageColumn.setCellFactory(col -> new TableCell<ProductView, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Image image = new Image("file:" + item);
                    imageView.setImage(image);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    setGraphic(imageView);
                }
            }
        });
        fetchProductDetails();
    }

    public void fetchProductDetails() {
        String query = """
                SELECT
                     p.name AS product_name,
                     p.product_id AS product_id,
                     s.size AS size_name,
                     v.quantity AS stock_quantity,
                     v.price AS product_price,
                     p.is_new AS isNew,
                     v.updated_at AS updated_date,
                     v.code AS product_code,
                     d.discount_percentage AS discount_status,
                     p.description AS product_description,
                     i.image AS product_image
                 FROM products p
                 JOIN variants v ON p.product_id = v.product_id
                 JOIN sizes s ON v.size_id = s.size_id
                 LEFT JOIN discounts d ON v.discount_id = d.discount_id
                 LEFT JOIN images i ON p.product_id = i.product_id
                 WHERE i.image_id IN (
                     SELECT MIN(image_id)
                     FROM images
                     GROUP BY product_id
                 )
                 ORDER BY v.updated_at DESC;
                """;
        Connection connection = MySQLConnection.connect();
        try {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String productName = resultSet.getString("product_name");
                    String size = resultSet.getString("size_name");
                    int stockQuantity = resultSet.getInt("stock_quantity");
                    double price = resultSet.getDouble("product_price");
                    String updatedDate = resultSet.getString("updated_date");
                    int discountStatus = resultSet.getInt("discount_status");
                    int isNewStatus = resultSet.getInt("isNew");
                    String description = resultSet.getString("product_code");
                    String discount = discountStatus == 1 ? "Có" : "Không";
                    String isNew = isNewStatus == 1 ? "Có" : "Không";
                    String productImage = resultSet.getString("product_image");
                    ProductView product = new ProductView(productName, size, stockQuantity, price, updatedDate, discount, description, productImage,isNew);
                    productList.add(product);
                }
                productTableView.setItems(productList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleAddImage(javafx.event.ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        List<File> selectedFiles = (List<File>) Config.showFileChooser(currentStage);
        if (selectedFiles != null) {
            for (File selectedFile : selectedFiles) {
                imageListView.getItems().add(String.valueOf(selectedFile));
                images.add(String.valueOf(selectedFile));
            }
        }
    }
}
