package com.example.demo.controller.admin.addproduct;

import com.example.demo.DAO.*;
import com.example.demo.Utils.Config;
import com.example.demo.classInterFace.initDataInterface;
import com.example.demo.config.MySQLConnection;
import com.example.demo.model.*;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.DAO.DiscountDAO.findDiscountById;
import static com.example.demo.Utils.Config.getCurrentDate;
import static com.example.demo.Utils.Config.hashCodeSHA;
import static com.example.demo.Utils.Modal.closeAllModals;
import static com.example.demo.Utils.Modal.showAlert;

public class UpdateProductController implements initDataInterface<ProductSearch> {
    @FXML
    public TextField sizeField;
    @FXML
    public TextField priceField;
    @FXML
    public TextField productQuantityField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField discountQuantityField;
    @FXML
    private TextField discountRemainingField;
    @FXML
    private TextField discountPercentageField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ListView<ImageView> imageListView;
    @FXML
    private CheckBox isNewCheckBox;
    @FXML
    private final List<String> images = new ArrayList<>();
    Connection connection = null;
    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final SizeDAO sizeDao = new SizeDAO();
    private final VariantDAO variantDAO = new VariantDAO();

    ProductSearch editData = null;

    @Override
    public void initData(ProductSearch data) throws SQLException {
        editData = data;
        connection = MySQLConnection.connect();
        if (data != null) {
            Discount discount = findDiscountById(connection, data.discountIdProperty());
            nameField.setText(data.getTenSanPham());
            descriptionField.setText(data.getProductDescription());
            categoryField.setText(data.getLoai());
            if (discount != null) {
                discountQuantityField.setText(String.valueOf(discount.getDiscountQuantity()));
                discountPercentageField.setText(String.valueOf(discount.getDiscountPercentage()));
                discountRemainingField.setText(String.valueOf(discount.getDiscountRemaining()));
                if (discount.getStartDate() != null) {
                    startDatePicker.setValue(discount.getStartDate());
                }
                if (discount.getEndDate() != null) {
                    endDatePicker.setValue(discount.getEndDate());
                }
            }
            if (data.getImage() != null && !data.getImage().isEmpty()) {
                Image image = new Image("file:" + data.getImage());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(120);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                imageListView.getItems().add(imageView);
                images.add(data.getImage());
            }

            isNewCheckBox.setSelected(true);
            sizeField.setText(data.getSize());
            IntegerProperty productQty = data.soLuongProperty();
            double price = data.getGia();
            priceField.setText(String.valueOf(price));
            productQuantityField.setText(String.valueOf(productQty.get()));
        }
    }

    public void handleAddImage(ActionEvent actionEvent) {
        imageListView.getItems().clear();
        images.clear();
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        List<File> selectedFiles = (List<File>) Config.showFileChooser(currentStage);

        if (selectedFiles != null) {
            for (File selectedFile : selectedFiles) {
                Image image = new Image(String.valueOf(selectedFile.toURI()));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(120);
                imageView.setFitHeight(100);
                imageListView.getItems().add(imageView);
                images.add(selectedFile.getAbsolutePath());
            }
        }
    }

    public void handleAddProduct(ActionEvent actionEvent) {
        String productName = nameField.getText();
        String description = descriptionField.getText();
        String selectedCategory = categoryField.getText();
        String price = priceField.getText();
        String quantity = productQuantityField.getText();
        String sizeF = sizeField.getText();
        boolean isNew = isNewCheckBox.isSelected();
        String finalDiscountId = null;
        Connection connection = MySQLConnection.connect();

        double discountPercentage = (discountPercentageField.getText() != null && !discountPercentageField.getText().isEmpty())
                ? Double.parseDouble(discountPercentageField.getText())
                : 0.0;
        int discountQuantity = (discountQuantityField.getText() != null && !discountQuantityField.getText().isEmpty()) ? Integer.parseInt(discountQuantityField.getText()) : 0;
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (productName.isEmpty() || images.isEmpty() || description.isEmpty() || sizeF.isEmpty() || price.isEmpty() || selectedCategory == null || selectedCategory.isEmpty()) {
            showAlert("Hãy nhập đủ các thông tin và thử lại!");
            return;
        }

        try {
            Category category = categoryDAO.getCategoryByName(connection, selectedCategory);
            if (category == null) {
                Category newCategory = new Category(0, selectedCategory, null, "Category description");
                category = categoryDAO.addCategory(connection, newCategory);
            }

            int categoryId = category.getCategoryId();

            Product newProduct = new Product(productName, description, categoryId, isNew);
            productDAO.updateProduct(connection, newProduct, editData.getProductId());
            if (discountPercentage > 0 && discountQuantity > 0) {
                if (startDate == null || endDate == null) {
                    showAlert("Xin vui lòng chọn nhày bắt đầu và ngày kết thúc giảm giá cho sản phẩm này!");
                    return;
                } else if (!endDate.isAfter(startDate)) {
                    showAlert("Ngày kết thúc phải lớn hơn ngày bắt đầu. Vui lòng chọn lại!");
                    return;
                } else {
                    Discount newDiscount = new Discount(discountPercentage, discountQuantity, discountQuantity, startDate, endDate);
                    int discountId = DiscountDAO.addDiscount(newDiscount);
                    finalDiscountId = (discountId != -1) ? Integer.toString(discountId) : null;
                }
            }

            productDAO.updateProductImageById(connection, editData.getImageId(), images.getFirst());
            Size existingSize = sizeDao.getSizeByName(connection, sizeF);
            if (existingSize != null) {
                String sizeId = String.valueOf(existingSize.getSizeId());
                variantDAO.updateProductVariant(connection, editData.getProductId(), sizeId, price, Integer.parseInt(quantity), finalDiscountId);
            } else {
                Size sz = new Size(1, sizeF, "");
                int newSizeId = sizeDao.addSize(connection, sz);
                if (newSizeId != -1) {
                    variantDAO.updateProductVariant(connection, editData.getProductId(), String.valueOf(newSizeId), price, Integer.parseInt(quantity), finalDiscountId);
                }
            }
            showAlert("Sửa thông tin sản phẩm thành công", () -> {
                imageListView.getItems().clear();
                images.clear();
                try {
                    AddProductTestController.loadDataproduct(null, 1);
                    closeAllModals();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                closeAllModals();
            });
        } catch (NumberFormatException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
