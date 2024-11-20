package com.example.demo.config.loading;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;

public class LoadingOverlay {

    private final StackPane overlay;

    public LoadingOverlay() {
        overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle("-fx-progress-color: black;");
        overlay.getChildren().add(progressIndicator);
        overlay.setVisible(false);
    }

    public void addTo(StackPane parent) {
        if (!parent.getChildren().contains(overlay)) {
            parent.getChildren().add(overlay);
        }
    }

    public void show() {
        overlay.setVisible(true);
    }

    public void hide() {
        overlay.setVisible(false);
    }
}
