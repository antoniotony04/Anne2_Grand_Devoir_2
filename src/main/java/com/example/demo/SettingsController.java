package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class SettingsController {

    private static String backgroundColor = "-fx-background-color: white;"; // Default color
    private Pane mainPane;

    public void setMainPane(Pane mainPane) {
        this.mainPane = mainPane;
    }

    @FXML
    private void setBlueBackground() {
        updateBackground("-fx-background-color: blue;");
    }

    @FXML
    private void setRedBackground() {
        updateBackground("-fx-background-color: red;");
    }

    @FXML
    private void setWhiteBackground() {
        updateBackground("-fx-background-color: white;");
    }

    private void updateBackground(String color) {
        backgroundColor = color;
        if (mainPane != null) {
            mainPane.setStyle(backgroundColor);
        }
    }

    public static String getBackgroundColor() {
        return backgroundColor;
    }
}
