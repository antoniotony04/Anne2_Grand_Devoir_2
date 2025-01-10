package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

public class GameIntegration {

    @FXML
    private GridPane gameGrid; // Grid pentru hartă

    @FXML
    private ListView<String> inventoryList; // ListView pentru inventar

    private GameInteractions gameInteractions;

    private Object[][] map; // Harta jocului

    // Lista pentru inventar
    private ObservableList<String> inventory = FXCollections.observableArrayList();
    private ObservableList<String> inventoryNames = FXCollections.observableArrayList();
    private ObservableList<Integer> inventoryCounts = FXCollections.observableArrayList();
    private ObservableList<String> formattedInventory = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        int mapSize = 10;
        map = new Object[mapSize][mapSize];
        initializeMap(mapSize);

        // Poziția inițială a jucătorului
        int playerStartX = mapSize / 2;
        int playerStartY = mapSize / 2;
        map[playerStartY][playerStartX] = "Player";

        System.out.println("Jucătorul a fost plasat pe hartă la poziția: (" + playerStartX + ", " + playerStartY + ")");

        // Inițializare GameInteractions
        gameInteractions = new GameInteractions(map, playerStartX, playerStartY, this);


        // Legăm lista logică de ListView
        inventoryList.setItems(formattedInventory); // Legăm lista formatată de ListView


        drawMap();
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        gameInteractions.handleMovement(event);
        drawMap();
    }

    void handleCellInteraction(int x, int y) {
        Object encountered = map[y][x];

        if (encountered == null) {
            System.out.println("Te-ai mutat pe o celulă goală.");
        } else if ("Tree".equals(encountered)) {
            addItemToInventory("Lemn");
            map[y][x] = null;
        } else if ("Rock".equals(encountered)) {
            addItemToInventory("Piatră");
            map[y][x] = null;
        } else if ("Grain".equals(encountered)) {
            addItemToInventory("Grâne");
            map[y][x] = null;
        } else if ("Enemy".equals(encountered)) {
            System.out.println("Ai întâlnit un inamic! Lupta începe.");
            // Logică pentru luptă
        }
    }



    private void initializeMap(int size) {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                double randomValue = Math.random();
                if (randomValue < 0.1) {
                    map[y][x] = "Tree";
                } else if (randomValue < 0.2) {
                    map[y][x] = "Rock";
                } else if (randomValue < 0.3) {
                    map[y][x] = "Grain";
                } else if (randomValue < 0.4) {
                    map[y][x] = "Enemy";
                } else {
                    map[y][x] = null;
                }
            }
        }
    }

    private void drawMap() {
        gameGrid.getChildren().clear(); // Curățăm GridPane-ul
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                javafx.scene.shape.Rectangle cell = new javafx.scene.shape.Rectangle(50, 50);
                cell.setStroke(javafx.scene.paint.Color.BLACK);

                if (map[y][x] == null) {
                    cell.setFill(javafx.scene.paint.Color.LIGHTGRAY);
                } else if ("Player".equals(map[y][x])) {
                    cell.setFill(javafx.scene.paint.Color.PINK); // Jucătorul este reprezentat cu roz
                } else if ("Tree".equals(map[y][x])) {
                    cell.setFill(javafx.scene.paint.Color.GREEN);
                } else if ("Rock".equals(map[y][x])) {
                    cell.setFill(javafx.scene.paint.Color.GRAY);
                } else if ("Grain".equals(map[y][x])) {
                    cell.setFill(javafx.scene.paint.Color.YELLOW);
                } else if ("Enemy".equals(map[y][x])) {
                    cell.setFill(javafx.scene.paint.Color.RED);
                }

                gameGrid.add(cell, x, y);
            }
        }
    }
    private void addItemToInventory(String itemName) {
        int index = inventoryNames.indexOf(itemName);

        if (index >= 0) {
            // Obiectul există deja în inventar
            inventoryCounts.set(index, inventoryCounts.get(index) + 1);
        } else {
            // Adăugăm un obiect nou
            inventoryNames.add(itemName);
            inventoryCounts.add(1);
        }

        updateFormattedInventory();
    }

    private void updateFormattedInventory() {
        formattedInventory.clear();

        for (int i = 0; i < inventoryNames.size(); i++) {
            String formattedItem = inventoryNames.get(i) + " (" + inventoryCounts.get(i) + ")";
            formattedInventory.add(formattedItem);
        }
    }

}