package com.example.demo.controller.user.endday;

import com.example.demo.Utils.Modal;
import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.model.Shift;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.List;

import static com.example.demo.Utils.Modal.closeModal;

public class EndDayController {

    @FXML
    private void EndDaySuccess(ActionEvent event) {
        List<Shift> shifts = PreferencesUtils.getShiftList();
        int count = shifts.size();
        if(count > 2) {
            PreferencesUtils.clearShiftList();
            PreferencesUtils.clearshift();
            closeModal();
        }else{
            Modal.showAlert("Chưa đạt đủ số ca trong ngày");
        }
    }
}
