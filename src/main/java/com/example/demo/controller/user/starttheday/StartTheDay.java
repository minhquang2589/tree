package com.example.demo.controller.user.starttheday;

import com.example.demo.controller.user.SalesDashboardLayoutController;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.time.LocalDate;
import com.example.demo.Utils.Modal;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.io.IOException;

public class StartTheDay {

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField textFieldDate;

    @FXML
    private void onStartTheDaySuccess(ActionEvent actionEvent) throws IOException{

        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            textFieldDate.setText(selectedDate.toString());
            SalesDashboardLayoutController salesDashboardController = getSalesDashboardController();
            if (salesDashboardController != null) {
                salesDashboardController.updateSalesDateField(selectedDate);
                Modal.showModal("/com/example/demo/controller/auth/view/user/starttheday/startthedaysuccess/startthedaysuccess.fxml", "");
            }
            Stage stage = (Stage) textFieldDate.getScene().getWindow();
            stage.close();
        } else {
            textFieldDate.setPromptText("Vui lòng chọn ngày!");
        }
    }
    private SalesDashboardLayoutController getSalesDashboardController() {
        return SalesDashboardLayoutController.getInstance();

    }
}