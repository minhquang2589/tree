package com.example.demo.controller.user.report;

import com.example.demo.Utils.Modal;
import javafx.event.ActionEvent;

import java.io.IOException;

public class ReportController {

    public void onShowReport(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/user/report/showreport/showreport.fxml", "Tính tiền");
    }
}
