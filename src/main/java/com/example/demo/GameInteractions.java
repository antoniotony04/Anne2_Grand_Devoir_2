package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameInteractions {

    private Object[][] map; // Referință la hartă
    private int playerX, playerY; // Coordonatele jucătorului
    private ObservableList<String> inventory = FXCollections.observableArrayList();
    private GameIntegration gameIntegration;
    public GameInteractions(Object[][] map, int startX, int startY, GameIntegration gameIntegration) {
        this.map = map;
        this.playerX = startX;
        this.playerY = startY;
        this.gameIntegration = gameIntegration;
    }

    public void handleMovement(KeyEvent event) {
        int newX = playerX;
        int newY = playerY;

        if (event.getCode() == KeyCode.W) {
            newY--;
        } else if (event.getCode() == KeyCode.S) {
            newY++;
        } else if (event.getCode() == KeyCode.A) {
            newX--;
        } else if (event.getCode() == KeyCode.D) {
            newX++;
        }

        if (isValidMove(newX, newY)) {
            Object encountered = map[newY][newX];

            if (encountered != null) {
                // Apelăm metoda handleCellInteraction din GameIntegration
                gameIntegration.handleCellInteraction(newX, newY);
            }

            // Mutăm jucătorul
            updatePlayerPosition(newX, newY);
        }
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < map[0].length && y >= 0 && y < map.length;
    }

    private void handleCellInteraction(int x, int y) {
        Object encountered = map[y][x];

        if (encountered == null) {
            System.out.println("Te-ai mutat pe o celulă goală.");
        } else if ("Tree".equals(encountered)) {
            System.out.println("Ai colectat lemn.");
            inventory.add("Lemn"); // Adăugăm în inventar
            System.out.println("Inventar: " + inventory); // Debugging
            map[y][x] = null;
        } else if ("Rock".equals(encountered)) {
            System.out.println("Ai colectat piatră.");
            inventory.add("Piatră"); // Adăugăm în inventar
            System.out.println("Inventar: " + inventory); // Debugging
            map[y][x] = null;
        } else if ("Grain".equals(encountered)) {
            System.out.println("Ai colectat grâne.");
            inventory.add("Grâne"); // Adăugăm în inventar
            System.out.println("Inventar: " + inventory); // Debugging
            map[y][x] = null;
        } else if ("Enemy".equals(encountered)) {
            System.out.println("Ai întâlnit un inamic! Lupta începe.");
            // Logică pentru luptă
        }
    }



    private void updatePlayerPosition(int newX, int newY) {
        map[playerY][playerX] = null; // Golim celula veche
        playerX = newX;
        playerY = newY;
        map[playerY][playerX] = "Player"; // Actualizăm poziția jucătorului
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }
}
