package com.example.demo.controller.admin.addproduct;

import java.io.IOException;
import java.sql.*;

import com.example.demo.Utils.Modal;
import com.example.demo.config.MySQLConnection;
import com.example.demo.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import static com.example.demo.Utils.Config.formatCurrencyVND;
import static com.example.demo.Utils.Modal.*;

public class AddProductTestController {
    @FXML
    public TableView<ProductSearch> productTable;
    @FXML
    public static ObservableList<ProductSearch> productList = FXCollections.observableArrayList();
    @FXML
    public TableColumn actionColumn;
    @FXML
    public TableColumn<ProductSearch, String> codeColumn;
    @FXML
    public TextField searchField;
    @FXML
    public Button backButton;
    @FXML
    private TableColumn<ProductSearch, String> nameColumn;
    @FXML
    private TableColumn<ProductSearch, String> imageColumn;
    @FXML
    private TableColumn<ProductSearch, String> categoryColumn;
    @FXML
    private TableColumn<ProductSearch, String> sizeColumn;
    @FXML
    private TableColumn<ProductSearch, Double> discountPriceColumn;
    @FXML
    private TableColumn<ProductSearch, Integer> quantityColumn;
    @FXML
    private TableColumn<ProductSearch, Double> discountColumn;
    @FXML
    private TableColumn<ProductSearch, Double> priceColumn;
    @FXML
    private Label pageLabel;

    @FXML
    private static int currentPage = 1;


    public void initialize() throws IOException, SQLException {
        loadDataproduct(null, 1);
        updatePageLabel(currentPage);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue.trim().isEmpty()) {
                    loadDataproduct(null, 1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        productList.addListener((ListChangeListener<ProductSearch>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved() || change.wasUpdated()) {
                    updateProductTableView();
                }
            }
        });
        updateProductTableView();

    }

    private void updatePageLabel(int page) {
        backButton.setVisible(page > 1);
        pageLabel.setText("Page: " + page);
    }

    @FXML
    private void onNextPage() {
        currentPage++;
        try {
            loadDataproduct(null, currentPage);
            updatePageLabel(currentPage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onPreviousPage() {
        if (currentPage > 1) {
            currentPage--;
            try {
                loadDataproduct(null, currentPage);
                updatePageLabel(currentPage);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadDataproduct(String codeOrNameSearch, int page) throws SQLException {
        Connection connection = MySQLConnection.connect();
        productList.clear();
        int itemsPerPage = 20;
        if (page <= 1) {
            page = 1;
        }
        int offset = (page - 1) * itemsPerPage;

        String query = """
                SELECT
                    p.product_id,
                    p.description AS pro_des,
                    p.name AS product_name,
                    p.category_id AS pCateId,
                    c.category AS category_name,
                    c.category,
                    s.size AS size_name,
                    v.variant_id,
                    v.size_id as vSizeId,
                    v.quantity,
                    v.discount_id,
                    v.price,
                    v.code,
                    d.discount_percentage,
                    i.image,
                    i.image_id
                FROM products p
                JOIN categories c ON p.category_id = c.category_id
                JOIN variants v ON p.product_id = v.product_id
                JOIN sizes s ON v.size_id = s.size_id
                LEFT JOIN discounts d ON v.discount_id = d.discount_id
                LEFT JOIN images i ON p.product_id = i.product_id
                WHERE i.image_id = (
                    SELECT MIN(image_id)
                    FROM images
                    WHERE product_id = p.product_id
                )
                """;


        if (codeOrNameSearch != null && !codeOrNameSearch.isEmpty()) {
            query += " WHERE v.code LIKE ? OR p.name LIKE ?";
        }

        query += " LIMIT ? OFFSET ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            if (codeOrNameSearch != null && !codeOrNameSearch.isEmpty()) {
                String searchPattern = "%" + codeOrNameSearch + "%";
                preparedStatement.setString(1, searchPattern);
                preparedStatement.setString(2, searchPattern);
                preparedStatement.setInt(3, itemsPerPage);
                preparedStatement.setInt(4, offset);
            } else {
                preparedStatement.setInt(1, itemsPerPage);
                preparedStatement.setInt(2, offset);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String category = resultSet.getString("category_name");
                int variantId = resultSet.getInt("variant_id");
                String size = resultSet.getString("size_name");
                String productName = resultSet.getString("product_name");
                double price = resultSet.getDouble("price");
                int qty = resultSet.getInt("quantity");
                double discountPercentage = resultSet.getDouble("discount_percentage");
                double totalAmount = price * qty * (1 - (discountPercentage / 100));
                String imageUrl = resultSet.getString("image");
                String discountId = resultSet.getString("discount_id");
                String productId = resultSet.getString("product_id");
                String code = resultSet.getString("code");
                String des = resultSet.getString("pro_des");
                String sizeId = resultSet.getString("vSizeId");
                String cateId = resultSet.getString("pCateId");
                String imageId = resultSet.getString("image_id");
                ProductSearch newProduct = new ProductSearch(1, productName, imageUrl, category, price, qty, discountPercentage, totalAmount, size, variantId, discountId, productId, code, des, sizeId, cateId, imageId);
                productList.add(newProduct);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateProductTableView() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().tenSanPhamProperty());
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().loaiProperty());
        sizeColumn.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
        codeColumn.setCellValueFactory(cellData -> cellData.getValue().getCode());
        discountColumn.setCellValueFactory(cellData -> cellData.getValue().chietKhauProperty().asObject());
        discountColumn.setCellFactory(column -> new TableCell<ProductSearch, Double>() {
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
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().soLuongProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().thanhTienProperty().asObject());
        priceColumn.setCellFactory(column -> new TableCell<ProductSearch, Double>() {
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
        discountPriceColumn.setCellValueFactory(cellData -> cellData.getValue().giaProperty().asObject());
        discountPriceColumn.setCellFactory(column -> new TableCell<ProductSearch, Double>() {
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

        imageColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getImage()));
        imageColumn.setCellFactory(col -> new TableCell<ProductSearch, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    javafx.scene.image.Image image = new Image("file:" + item);
                    imageView.setImage(image);
                    imageView.setFitHeight(125);
                    imageView.setFitWidth(160);
                    setGraphic(imageView);
                }
            }
        });
        actionColumn.setCellFactory(new Callback<TableColumn<ProductSearch, String>, TableCell<ProductSearch, String>>() {
            @Override
            public TableCell<ProductSearch, String> call(TableColumn<ProductSearch, String> param) {
                return new TableCell<ProductSearch, String>() {
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
        productTable.setItems(productList);
    }

    private void handleEdit(ProductSearch item) throws IOException, SQLException {
        showModalWithData("/com/example/demo/controller/auth/view/admin/addproduct/edit-product-view.fxml", "Sửa thông tin sản phẩm", item, () -> {
        });
    }

    private void handleDelete(ProductSearch item) {
        System.out.println(item);
        Modal.showAlert(null, "Bạn có chắc chắn muốn xoá sản phầm này không?", Alert.AlertType.CONFIRMATION, () -> {
            String query = "DELETE FROM products WHERE product_id = " + item.getProductId();
            Connection connection = MySQLConnection.connect();
            try {
                assert connection != null;
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(query);
                    productList.remove(item);
                }

            } catch (SQLException e) {
                showAlert(null);
                e.printStackTrace();
            }
        }, null);
    }


    public void uploadOnClick(ActionEvent actionEvent) throws IOException {
        showModal("/com/example/demo/controller/auth/view/admin/addproduct/add-product-test-view.fxml", "Tải lên sản phẩm", () -> {
            try {
                loadDataproduct(null, 1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void onSearchButtonClick(ActionEvent actionEvent) throws SQLException {
        String searchInput = searchField.getText();
        loadDataproduct(searchInput, 1);
    }
}
