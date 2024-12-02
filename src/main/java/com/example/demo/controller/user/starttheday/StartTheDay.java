package com.example.demo.controller.user.starttheday;

import com.example.demo.Utils.Modal;
import javafx.event.ActionEvent;

import java.io.IOException;

public class StartTheDay {

    public void onStartTheDaySuccess(ActionEvent actionEvent) throws IOException {
        Modal.showModal("/com/example/demo/controller/auth/view/auth/starttheday/startthedaysuccess/startthedaysuccess.fxml", "");
    }
}
