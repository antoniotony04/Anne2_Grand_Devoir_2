package com.example.demo;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;

import java.io.IOException;
import java.util.Optional;

public class GameIntegration {

    @FXML
    private GridPane gameGrid;

    @FXML
    private ListView<String> inventoryList;

    private GameInteractions gameInteractions;

    private Object[][] map;
    @FXML
    private ListView<String> statsList;
    private ObservableList<String> playerStats = FXCollections.observableArrayList();
    public Player player = new Player();

    private ObservableList<String> inventory = FXCollections.observableArrayList();
    private ObservableList<String> inventoryNames = FXCollections.observableArrayList();
    private ObservableList<Integer> inventoryCounts = FXCollections.observableArrayList();
    private ObservableList<String> formattedInventory = FXCollections.observableArrayList();
    private int score = 0;
    @FXML
    private Label scoreLabel;
    @FXML
    private BorderPane mainPane;


    @FXML
    public void initialize() {
        int mapSize = 10;
        map = new Object[mapSize][mapSize];
        initializeMap(mapSize);
        mainPane.setStyle(SettingsController.getBackgroundColor());

        int playerStartX = mapSize / 2;
        int playerStartY = mapSize / 2;
        map[playerStartY][playerStartX] = "Player";

        System.out.println("Jucatorul a fost plasat pe harta la poziția: (" + playerStartX + ", " + playerStartY + ")");


        gameInteractions = new GameInteractions(map, playerStartX, playerStartY, this);

        scoreLabel.setText("Scor: " + score);
        inventoryList.setItems(formattedInventory);

        updatePlayerStats();
        statsList.setItems(playerStats);
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
            player.addItemToInventory("Lemn");
            System.out.println("Ai colectat Lemn.");
            map[y][x] = null;
            updateFormattedInventory();
        } else if ("Rock".equals(encountered)) {
            player.addItemToInventory("Piatră");
            System.out.println("Ai colectat Piatră.");
            map[y][x] = null;
            updateFormattedInventory();
        } else if ("Grain".equals(encountered)) {
            player.addItemToInventory("Grâne");
            System.out.println("Ai colectat Grâne.");
            map[y][x] = null;
            updateFormattedInventory();
        } else if ("Enemy".equals(encountered)) {
            System.out.println("Ai întâlnit un inamic! Lupta începe.");
            startCombat(x, y);
        }


        checkIfGameWon();
    }

    private void startCombat(int x, int y) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("combat-window.fxml"));
            Parent root = loader.load();

            CombatController controller = loader.getController();
            controller.setGameIntegration(this);
            controller.setPlayer(player);


            controller.setOnCombatEnd(() -> {
                if (player.getHp() > 0) {

                    System.out.println("Ai câștigat lupta!");
                    map[y][x] = null;
                    drawMap();

                    Item loot = generateRandomLoot();
                    System.out.println("Ai primit: " + loot.getName());

                    askToEquipItem(loot);


                    player.addItemToInventory(loot.getName());
                    updateFormattedInventory();
                } else {
                    System.out.println("Ai pierdut lupta!");
                    showGameOverScreen();
                }
            });

            Stage combatStage = new Stage();
            combatStage.setTitle("Lupta");
            combatStage.setScene(new Scene(root));
            combatStage.show();
        } catch (IOException e) {
            System.out.println("Eroare la deschiderea ferestrei de lupta: " + e.getMessage());
        }
    }


    void askToEquipItem(Item item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Echipare Item");
        alert.setHeaderText("Ai primit un item nou: " + item.getName());
        alert.setContentText("Vrei sa echipezi acest item?");

        ButtonType buttonYes = new ButtonType("Da");
        ButtonType buttonNo = new ButtonType("Nu", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonYes) {
            player.equipItem(item);
            System.out.println("Ai echipat: " + item.getName());
            updatePlayerStats();
            updateFormattedInventory();
        } else {
            player.addItemToInventory(item.getName());
            updateFormattedInventory();
            System.out.println("Ai adaugat în inventar: " + item.getName());
        }
    }




    public Item generateRandomLoot() {
        Item[] lootTable = {
                new Item("Sabie a Inamicului", "Arma", 0, 0, 15),
                new Item("Casca a Inamicului", "Casca", 5, 0, 0),
                new Item("Armura a Inamicului", "Armura", 10, 10, 0)
        };


        int randomIndex = (int) (Math.random() * lootTable.length);
        return lootTable[randomIndex];
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
        gameGrid.getChildren().clear();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                javafx.scene.shape.Rectangle cell = new javafx.scene.shape.Rectangle(50, 50);
                cell.setStroke(javafx.scene.paint.Color.BLACK);

                if (map[y][x] == null) {
                    cell.setFill(javafx.scene.paint.Color.LIGHTGRAY);
                } else if ("Player".equals(map[y][x])) {
                    cell.setFill(javafx.scene.paint.Color.PINK);
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

            inventoryCounts.set(index, inventoryCounts.get(index) + 1);
        } else {
            inventoryNames.add(itemName);
            inventoryCounts.add(1);
        }

        updateFormattedInventory();
    }

    void updateFormattedInventory() {
        formattedInventory.clear();


        if (player.getInventoryNames().size() != player.getInventoryCounts().size()) {
            System.out.println("Eroare: Dimensiunile listelor inventarului nu coincid!");
            return;
        }

        for (int i = 0; i < player.getInventoryNames().size(); i++) {
            String formattedItem = player.getInventoryNames().get(i) + " (" + player.getInventoryCounts().get(i) + ")";
            formattedInventory.add(formattedItem);
        }

        inventoryList.setItems(formattedInventory);
    }
    private void showVictoryMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Joc Terminat");
        alert.setHeaderText("Felicitări!");
        alert.setContentText("Ai câștigat jocul! Scor: " + score);

        alert.showAndWait();


        Platform.exit();
    }

    private void checkIfGameWon() {
        boolean isMapEmpty = true;


        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] != null && !"Player".equals(map[y][x])) {
                    isMapEmpty = false;
                    break;
                }
            }
            if (!isMapEmpty) break;
        }


        if (isMapEmpty) {
            showVictoryMessage();
        }
    }



    public void saveGame() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("save.txt"))) {

            writer.write(gameInteractions.getPlayerX() + " " + gameInteractions.getPlayerY());
            writer.newLine();


            writer.write(player.getHp() + " " + player.getArmor() + " " + player.getAttack());
            writer.newLine();


            StringBuilder equippedItems = new StringBuilder();
            for (Item item : player.getEquippedItems()) {
                equippedItems.append(item.getName()).append(",")
                        .append(item.getType()).append(",")
                        .append(item.getHpBonus()).append(",")
                        .append(item.getArmorBonus()).append(",")
                        .append(item.getAttackBonus()).append(";");
            }
            writer.write(equippedItems.toString());
            writer.newLine();


            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[y].length; x++) {
                    writer.write((map[y][x] == null ? "null" : map[y][x].toString()) + " ");
                }
                writer.newLine();
            }


            for (int i = 0; i < player.getInventoryNames().size(); i++) {
                writer.write(player.getInventoryNames().get(i) + " " + player.getInventoryCounts().get(i));
                writer.newLine();
            }

            System.out.println("Jocul a fost salvat cu succes.");
        } catch (IOException e) {
            System.out.println("Eroare la salvarea jocului: " + e.getMessage());
        }
    }







    public void loadGame() {
        try (BufferedReader reader = new BufferedReader(new FileReader("save.txt"))) {

            String[] position = reader.readLine().split(" ");
            int playerX = Integer.parseInt(position[0]);
            int playerY = Integer.parseInt(position[1]);
            gameInteractions.setPlayerPosition(playerX, playerY);


            String[] playerAttributes = reader.readLine().split(" ");
            player.setBaseHp(Integer.parseInt(playerAttributes[0]));
            player.setBaseArmor(Integer.parseInt(playerAttributes[1]));
            player.setBaseAttack(Integer.parseInt(playerAttributes[2]));


            String equippedLine = reader.readLine();
            if (!equippedLine.isEmpty() && !equippedLine.equals("null")) {
                String[] equippedItems = equippedLine.split(";");
                for (String equippedItem : equippedItems) {
                    if (!equippedItem.trim().isEmpty()) {
                        String[] itemData = equippedItem.split(",");
                        if (itemData.length == 5) {
                            String name = itemData[0];
                            String type = itemData[1];
                            int hpBonus = Integer.parseInt(itemData[2]);
                            int armorBonus = Integer.parseInt(itemData[3]);
                            int attackBonus = Integer.parseInt(itemData[4]);

                            Item item = new Item(name, type, hpBonus, armorBonus, attackBonus);
                            player.equipItem(item);
                        } else {
                            System.out.println("Eroare: Format incorect pentru echipamente -> " + equippedItem);
                        }
                    }
                }
            }


            for (int y = 0; y < map.length; y++) {
                String[] line = reader.readLine().split(" ");
                for (int x = 0; x < map[y].length; x++) {
                    map[y][x] = line[x].equals("null") ? null : line[x];
                }
            }


            player.getInventoryNames().clear();
            player.getInventoryCounts().clear();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    player.addItemToInventory(parts[0]);
                    int count = Integer.parseInt(parts[1]);
                    int index = player.getInventoryNames().indexOf(parts[0]);
                    if (index >= 0) {
                        player.getInventoryCounts().set(index, count);
                    }
                } else {
                    System.out.println("Eroare: Format incorect pentru inventar -> " + line);
                }
            }


            updatePlayerStats();
            updateFormattedInventory();
            drawMap();

            System.out.println("Jocul a fost încarcat cu succes.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Eroare la încarcarea jocului: " + e.getMessage());
        }
    }






    void updatePlayerStats() {
        playerStats.clear();
        playerStats.add("HP: " + player.getHp());
        playerStats.add("Armura: " + player.getArmor());
        playerStats.add("Atac: " + player.getAttack());
        playerStats.add("Echipamente:");

        for (Item item : player.getEquippedItems()) {
            playerStats.add("- " + item.getName());
        }

        statsList.setItems(playerStats);
    }




    public void unequipItem(String itemType) {
        player.getEquippedItems().removeIf(item -> item.getType().equals(itemType));
        updatePlayerStats();
        System.out.println("Ai dezechipat item-ul de tip: " + itemType);
    }

    void updateScore() {
        scoreLabel.setText("Scor: " + score);
    }


    void showGameOverScreen() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Joc Terminat");
        alert.setHeaderText("Ai pierdut!");
        alert.setContentText("Scor final: " + score);

        alert.showAndWait();


        Platform.exit();
    }
    public void incrementScore() {
        score++;
    }
    public int getScore() {
        return score;
    }
    public Player getPlayer() {
        return player;
    }

    @FXML
    private void openCraftingWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("crafting-window.fxml"));
            Parent root = loader.load();

            CraftingController controller = loader.getController();
            controller.setGameIntegration(this);

            Stage craftingStage = new Stage();
            craftingStage.setTitle("Crafting");
            craftingStage.setScene(new Scene(root));
            craftingStage.show();
        } catch (IOException e) {
            System.out.println("Eroare la deschiderea ferestrei de crafting: " + e.getMessage());
        }
    }



}
