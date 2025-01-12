package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameInteractions {

    private Object[][] map;
    private int playerX, playerY;
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

                gameIntegration.handleCellInteraction(newX, newY);
            }


            updatePlayerPosition(newX, newY);
        }
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < map[0].length && y >= 0 && y < map.length;
    }

    private void updatePlayerPosition(int newX, int newY) {
        map[playerY][playerX] = null;
        playerX = newX;
        playerY = newY;
        map[playerY][playerX] = "Player";
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerPosition(int x, int y) {
        map[playerY][playerX] = null;
        playerX = x;
        playerY = y;
        map[playerY][playerX] = "Player";
    }

}
