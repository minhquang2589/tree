package com.example.demo.controller.user.endday;

import com.example.demo.Utils.Config;
import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.model.Shift;
import com.example.demo.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.sql.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.demo.Utils.Modal.closeModal;
import static com.example.demo.config.MySQLConnection.connect;

public class EndDayController {
    LocalDateTime currentTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedDateTime = currentTime.format(formatter);

    public void endshift() throws IOException {
        Connection connection = connect();
        if (connection != null) {
            String query = "UPDATE shifts SET end_date = '" + LocalDateTime.now() + "', revenue = " + Revenue() + " WHERE DATE(start_date) = '" + formattedDateTime + "' AND end_date IS NULL";
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @FXML
    private void EndDaySuccess(ActionEvent event) throws IOException {
        List<Shift> shifts = PreferencesUtils.getShiftList();
        int count = shifts.size();

        if(count >= 2) {
            endshift();

            Modal.showAlert("Kết thúc ngày thành công"+ System.lineSeparator()  + "Doanh thu của ngày: " + Revenue());

            PreferencesUtils.clearShiftList();
            PreferencesUtils.clearshift();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            closeModal(stage);
        } else {
            Modal.showAlert("Chưa đạt đủ số ca trong ngày");
        }
    }

    private int Revenue(){
        List<Shift> shifts = PreferencesUtils.getShiftList();
        int totalRevenue = 0;
        for (Shift shift : shifts) {
            totalRevenue += shift.getTong();
        }
        return totalRevenue;
    }
}