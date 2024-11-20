package com.example.demo.controller.admin;

import com.example.demo.DAO.StoreDAO;
import com.example.demo.Utils.Config;
import com.example.demo.Utils.Modal;
import com.example.demo.model.StoreModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StoreUploadModalController {

    @FXML
    public TextField nameField;
    @FXML
    public TextField phoneField;
    @FXML
    public TextField addressField;
    @FXML
    public DatePicker startDatePicker;
    @FXML
    public DatePicker endDatePicker;
    @FXML
    public ComboBox statusComboBox;
    public Button chooseImageButton;

    String imagePath;

    @FXML
    public void saveStore(ActionEvent event) throws SQLException {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String status = statusComboBox.toString();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateString = (startDate != null) ? startDate.format(formatter) : null;
        String endDateString = (endDate != null) ? endDate.format(formatter) : null;
        StoreModel newStore = new StoreModel(0, name, phone, address, startDateString, endDateString, imagePath, status);
        StoreDAO storeDAO = new StoreDAO();
        boolean isAdded = storeDAO.addStore(newStore);
        if (isAdded) {
            System.out.println("Cửa hàng đã được thêm thành công!");
        } else {
            System.out.println("Lỗi khi thêm cửa hàng.");
        }
    }


    public void openSelectImage(ActionEvent actionEvent) {
        File selectedFile = Config.showFileChooser((Stage) chooseImageButton.getScene().getWindow());
        if (selectedFile != null) {
            String savedImagePath = Config.saveImage(selectedFile.getName(), selectedFile);
            if (savedImagePath != null) {
                imagePath = savedImagePath;
            } else {
                Modal.showAlert(null);
            }
        }
    }


    @FXML
    public void closeModal(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
