package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class MainMenuController {

    private static final int MAP_SIZE = 10; // Dimensiunea hărții
    private Object[][] map; // Matricea ce conține obiectele

    @FXML
    private GridPane gameGrid; // GridPane pentru afișarea hărții
    @FXML
    private Pane mainPane; // Legat de root Pane-ul principal al aplicației


    @FXML
    public void initialize() {
        // Apply the saved background color on load
        mainPane.setStyle(SettingsController.getBackgroundColor());
    }

    @FXML
    public void resumeGame(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-integration.fxml"));
            Parent root = fxmlLoader.load();

            // Obține controller-ul scenei pentru a apela loadGame
            GameIntegration gameIntegration = fxmlLoader.getController();
            gameIntegration.loadGame();

            // Setează scena nouă
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (FileNotFoundException e) {
            System.out.println("Nu există un fișier de salvare.");
        } catch (IOException e) {
            System.out.println("Eroare la încărcarea scenei: " + e.getMessage());
        }
    }


    @FXML
    public void newGame(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-integration.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            // Setează focusul pe elementul principal
            root.requestFocus();

            // Obține fereastra principală și setează scena nouă
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void openOptions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
            Parent root = loader.load();

            // Pass the mainPane reference to the SettingsController
            SettingsController controller = loader.getController();
            controller.setMainPane(mainPane);

            Stage stage = new Stage();
            stage.setTitle("Setări");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Eroare la deschiderea ferestrei de setări: " + e.getMessage());
        }
    }


    @FXML
    public void showHelp(ActionEvent event) {
        // Logică pentru afișarea ajutorului
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText(null);
        alert.setContentText("Aici vor fi afișate instrucțiunile pentru joc.");
        alert.showAndWait();
    }

    @FXML
    public void exitGame(ActionEvent event) {
        // Închide aplicația
        System.exit(0);
    }

    private void generateNewMap() {
        map = new Object[MAP_SIZE][MAP_SIZE];
        Random random = new Random();

        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                double chance = random.nextDouble();
                if (chance < 0.1) {
                    map[y][x] = "Tree"; // Plasăm un copac
                } else if (chance < 0.2) {
                    map[y][x] = "Rock"; // Plasăm o piatră
                } else if (chance < 0.3) {
                    map[y][x] = "Grain"; // Plasăm grâne
                } else if (chance < 0.4) {
                    map[y][x] = "Enemy"; // Plasăm un inamic
                } else {
                    map[y][x] = null; // Celulă goală
                }
            }
        }
    }

    private void drawMap() {
        gameGrid.getChildren().clear(); // Curățăm gridul existent

        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                Rectangle cell = new Rectangle(50, 50);
                cell.setStroke(Color.BLACK);

                if (map[y][x] == null) {
                    cell.setFill(Color.LIGHTGRAY);
                } else if (map[y][x].equals("Tree")) {
                    cell.setFill(Color.GREEN);
                } else if (map[y][x].equals("Rock")) {
                    cell.setFill(Color.GRAY);
                } else if (map[y][x].equals("Grain")) {
                    cell.setFill(Color.YELLOW);
                } else if (map[y][x].equals("Enemy")) {
                    cell.setFill(Color.RED);
                }

                gameGrid.add(cell, x, y);
            }
        }
    }
}