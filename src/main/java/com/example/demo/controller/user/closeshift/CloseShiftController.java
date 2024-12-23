package com.example.demo.controller.user.closeshift;

import com.example.demo.Utils.Config;
import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.controller.user.SalesDashboardLayoutController;
import com.example.demo.model.Shift;
import com.example.demo.model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Utils.Modal.closeModal;
import static com.example.demo.Utils.Modal.showModal;
import static com.example.demo.config.MySQLConnection.connect;

public class CloseShiftController {

    @FXML
    private TextField textField;
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private TextField textField3;
    @FXML
    private TextField textField4;
    @FXML
    private TextField textField5;
    @FXML
    private TextField textField6;
    @FXML
    private TextField textField7;
    @FXML
    private TextField textField8;
    @FXML
    private TextField textField9;
    @FXML
    private TextField textField10;
    @FXML
    private TextField textField11;
    @FXML
    private TextField textField12;
    @FXML
    private TextField textField13;
    @FXML
    private TextField textField14;
    @FXML
    private TextField textField15;

    private TextField selectedTextField;

    @FXML
    private void selectTextField(MouseEvent event) {
        TextField clickedTextField = (TextField) event.getSource();
        selectedTextField = clickedTextField;
    }
    @FXML
    private void numberClick(ActionEvent event) {
        if (selectedTextField != null) {
            Button clickedButton = (Button) event.getSource();
            String buttonText = clickedButton.getText();
            selectedTextField.appendText(buttonText);
        }
    }

    @FXML
    private void clearText(ActionEvent event) {
        if (selectedTextField != null) {
            selectedTextField.clear();
        }
    }
    @FXML
    private void clearText1(ActionEvent event) {
        if (selectedTextField != null && !selectedTextField.getText().isEmpty()) {
            String currentText = selectedTextField.getText();
            if (currentText.length() > 0) {
                selectedTextField.deleteText(currentText.length() - 1, currentText.length());
            }
        }
    }
    @FXML
    private void calculateTotal() {
        try {
            double total = 0;
            total += parseInput(textField10.getText()) * 500_000;
            total += parseInput(textField6.getText()) * 200_000;
            total += parseInput(textField7.getText()) * 100_000;
            total += parseInput(textField8.getText()) * 50_000;
            total += parseInput(textField5.getText()) * 20_000;
            total += parseInput(textField9.getText()) * 10_000;
            total += parseInput(textField1.getText()) * 5_000;
            total += parseInput(textField.getText()) * 2_000;
            total += parseInput(textField4.getText()) * 1_000;
            total += parseInput(textField2.getText()) * 500;
            total += parseInput(textField3.getText()) * 200;
            total += 500000;
            textField14.setText(String.format("%,.0f", total));
        } catch (NumberFormatException e) {
            textField14.setText("Lỗi nhập liệu");
        }
    }
    private int parseInput(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(text.replace(" ", "").replace(",", ""));
    }

    @FXML
    public void closeshift(ActionEvent actionEvent) throws IOException {
        save_shift();
        Modal.showModal("/com/example/demo/controller/auth/view/user/closeshift/closeshiftsuccess/closeshiftsuccess.fxml","kết thúc ca",null);
    }
//
//    public void endshift() throws IOException {
//        Connection connection = connect();
//
//        if (connection != null) {
//            User user = PreferencesUtils.getUser();
//            assert user != null;
//
//
//            String query = "UPDATE shifts SET end_date = '" + LocalDateTime.now() + "' WHERE DATE(start_date) = '" + Config.getCurrentDate() + "'&& end_date IS NULL";
//            Statement statement = null;
//            try {
//                statement = connection.createStatement();
//                statement.executeUpdate(query);
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    public void save_shift() {
        // Retrieve existing shifts from preferences
        List<Shift> shifts = PreferencesUtils.getShiftList();

        // Create a new Shift object
        Shift shift = new Shift(
                parseInput(textField10.getText()),
                parseInput(textField6.getText()),
                parseInput(textField7.getText()),
                parseInput(textField8.getText()),
                parseInput(textField5.getText()),
                parseInput(textField9.getText()),
                parseInput(textField1.getText()),
                parseInput(textField.getText()),
                parseInput(textField4.getText()),
                parseInput(textField2.getText()),
                parseInput(textField3.getText()),
                parseInput(textField14.getText())
        );

        // Add the new shift to the existing list
        shifts.add(shift);

        // Save the updated list of shifts to preferences
        PreferencesUtils.saveShiftList(shifts);

        // Retrieve shifts from preferences again (after saving the new one)
        List<Shift> retrievedShifts = PreferencesUtils.getShiftList();

        // Print all shifts to verify they are retrieved correctly
        for (Shift shiftss : retrievedShifts) {
            System.out.println(shiftss);
        }
    }


}

