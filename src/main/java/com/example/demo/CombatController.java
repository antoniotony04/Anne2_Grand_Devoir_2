package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CombatController {

    @FXML
    private Label enemyHpLabel;

    @FXML
    private Label playerHpLabel;

    private GameIntegration gameIntegration;
    private int enemyHp = 50;
    private Player player;
    private Runnable onCombatEnd; // Callback pentru a închide fereastra și a continua jocul

    public void setGameIntegration(GameIntegration gameIntegration) {
        this.gameIntegration = gameIntegration;
    }


    public void setPlayer(Player player) {
        this.player = player;
        updateLabels();
    }

    public void setOnCombatEnd(Runnable onCombatEnd) {
        this.onCombatEnd = onCombatEnd;
    }

    @FXML
    private void handleAttack() {
        enemyHp -= player.getAttack(); // Jucatorul ataca inamicul
        player.takeDamage(10); // Inamicul ataca jucatorul

        updateLabels();

        if (enemyHp <= 0) {
            System.out.println("Inamicul a fost învins!");
            closeWindow();
        } else if (player.getHp() <= 0) {
            System.out.println("Ai pierdut lupta!");
            closeWindow();
        }
    }

    @FXML
    private void handleUseItem() {
        if (player.getInventoryNames().contains("Grâne")) {
            int index = player.getInventoryNames().indexOf("Grâne");
            int count = player.getInventoryCounts().get(index);

            if (count > 0) {
                player.getInventoryCounts().set(index, count - 1);
                player.setBaseHp(player.getHp() + 20); // Heal pentru 20 HP
                System.out.println("Ai folosit Grâne și te-ai vindecat cu 20 HP.");
                updateLabels();
                gameIntegration.updateFormattedInventory(); // Actualizam inventarul
            } else {
                System.out.println("Nu mai ai Grâne!");
            }
        } else {
            System.out.println("Nu ai Grâne în inventar!");
        }
    }

    private void updateLabels() {
        enemyHpLabel.setText("HP Inamic: " + Math.max(enemyHp, 0));
        playerHpLabel.setText("HP Jucator: " + Math.max(player.getHp(), 0));
    }

    private void closeWindow() {
        Stage stage = (Stage) enemyHpLabel.getScene().getWindow();

        if (enemyHp <= 0) {
            // Incrementam scorul
            gameIntegration.incrementScore();
            System.out.println("Ai câștigat lupta! Scorul tau: " + gameIntegration.getScore());

            // Drop de iteme
            gameIntegration.getPlayer().addItemToInventory("Sângele Inamicului");
            System.out.println("Ai primit 'Sângele Inamicului'.");

            // Generam un loot aleatoriu și întrebam daca dorim sa echipam
            Item drop = gameIntegration.generateRandomLoot();
            gameIntegration.askToEquipItem(drop);

            gameIntegration.updateFormattedInventory(); // Actualizam inventarul
            gameIntegration.updateScore(); // Actualizam scorul în interfața
        }

        if (gameIntegration.getPlayer().getHp() <= 0) {
            System.out.println("Jocul s-a terminat! Scor final: " + gameIntegration.getScore());
            gameIntegration.showGameOverScreen();
            stage.close();
            return;
        }

        stage.close();
    }



}