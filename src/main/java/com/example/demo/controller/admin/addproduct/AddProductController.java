package com.example.demo.controller.admin.addproduct;
import com.example.demo.config.MySQLConnection;
import com.example.demo.model.ProductView;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;

import java.sql.*;

public class AddProductController {

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
    private TableColumn<ProductView, String> descriptionColumn;

    private ObservableList<ProductView> productList;

    public void initialize() {
        productList = FXCollections.observableArrayList();

        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        sizeColumn.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().stockQuantityProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().updatedDateProperty());
        discountColumn.setCellValueFactory(cellData -> cellData.getValue().discountStatusProperty());
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
                     p.id AS product_id,
                     s.size AS size_name,
                     v.quantity AS stock_quantity,
                     p.price AS product_price,
                     p.is_new AS isNew,
                     v.updated_at AS updated_date,
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
                    String description = resultSet.getString("product_description");
                    String discount = discountStatus == 1 ? "Có" : "Không";
                    String productImage = resultSet.getString("product_image");

                    ProductView product = new ProductView(productName, size, stockQuantity, price, updatedDate, discount, description, productImage);
                    productList.add(product);
                }

                productTableView.setItems(productList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
