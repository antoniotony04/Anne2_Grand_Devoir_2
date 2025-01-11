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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;

import java.io.IOException;
import java.util.Optional;

public class GameIntegration {

    @FXML
    private GridPane gameGrid; // Grid pentru harta

    @FXML
    private ListView<String> inventoryList; // ListView pentru inventar

    private GameInteractions gameInteractions;

    private Object[][] map; // Harta jocului
    @FXML
    private ListView<String> statsList;
    private ObservableList<String> playerStats = FXCollections.observableArrayList();
    public Player player = new Player();
    // Lista pentru inventar
    private ObservableList<String> inventory = FXCollections.observableArrayList();
    private ObservableList<String> inventoryNames = FXCollections.observableArrayList();
    private ObservableList<Integer> inventoryCounts = FXCollections.observableArrayList();
    private ObservableList<String> formattedInventory = FXCollections.observableArrayList();
    private int score = 0; // Scorul începe de la 0
    @FXML
    private Label scoreLabel;


    @FXML
    public void initialize() {
        int mapSize = 10;
        map = new Object[mapSize][mapSize];
        initializeMap(mapSize);

        // Poziția inițiala a jucatorului
        int playerStartX = mapSize / 2;
        int playerStartY = mapSize / 2;
        map[playerStartY][playerStartX] = "Player";

        System.out.println("Jucatorul a fost plasat pe harta la poziția: (" + playerStartX + ", " + playerStartY + ")");

        // Inițializare GameInteractions
        gameInteractions = new GameInteractions(map, playerStartX, playerStartY, this);

        scoreLabel.setText("Scor: " + score); // Inițializare scor
        inventoryList.setItems(formattedInventory); // Legam lista formatata de ListView

        updatePlayerStats(); // Actualizam statisticile jucatorului
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
            System.out.println("Te-ai mutat pe o celula goala.");
        } else if ("Tree".equals(encountered)) {
            player.addItemToInventory("Lemn");
            System.out.println("Ai colectat Lemn.");
            map[y][x] = null;
            updateFormattedInventory();
        } else if ("Rock".equals(encountered)) {
            player.addItemToInventory("Piatra");
            System.out.println("Ai colectat Piatra.");
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
    }
    private void startCombat(int x, int y) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("combat-window.fxml"));
            Parent root = loader.load();

            CombatController controller = loader.getController();
            controller.setGameIntegration(this);
            controller.setPlayer(player);

            // Setam callback-ul pentru sfârșitul luptei
            controller.setOnCombatEnd(() -> {
                if (player.getHp() > 0) {
                    // Inamicul a fost învins
                    System.out.println("Ai câștigat lupta!");
                    map[y][x] = null; // Eliminam inamicul de pe harta
                    drawMap();

                    // Generam loot-ul
                    Item loot = generateRandomLoot();
                    System.out.println("Ai primit: " + loot.getName());

                    // Întreaba utilizatorul daca dorește sa echipeze itemul
                    askToEquipItem(loot);

                    // Adauga în inventar itemul droplat
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
            player.equipItem(item); // Echipam itemul
            System.out.println("Ai echipat: " + item.getName());
            updatePlayerStats(); // Actualizam statisticile
            updateFormattedInventory(); // Actualizam inventarul
        } else {
            // Adaugam itemul în inventar daca nu este echipat
            player.addItemToInventory(item.getName());
            updateFormattedInventory();
            System.out.println("Ai adaugat în inventar: " + item.getName());
        }
    }




    public Item generateRandomLoot() {
        // Definim posibilele loot-uri
        Item[] lootTable = {
                new Item("Sabie a Inamicului", "Arma", 0, 0, 15),
                new Item("Casca a Inamicului", "Casca", 5, 0, 0),
                new Item("Armura a Inamicului", "Armura", 10, 10, 0)
        };

        // Alegem un item aleatoriu
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
        gameGrid.getChildren().clear(); // Curațam GridPane-ul
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                javafx.scene.shape.Rectangle cell = new javafx.scene.shape.Rectangle(50, 50);
                cell.setStroke(javafx.scene.paint.Color.BLACK);

                if (map[y][x] == null) {
                    cell.setFill(javafx.scene.paint.Color.LIGHTGRAY);
                } else if ("Player".equals(map[y][x])) {
                    cell.setFill(javafx.scene.paint.Color.PINK); // Jucatorul este reprezentat cu roz
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
            // Obiectul exista deja în inventar
            inventoryCounts.set(index, inventoryCounts.get(index) + 1);
        } else {
            // Adaugam un obiect nou
            inventoryNames.add(itemName);
            inventoryCounts.add(1);
        }

        updateFormattedInventory();
    }

    void updateFormattedInventory() {
        formattedInventory.clear();

        // Verificam consistența între `inventoryNames` și `inventoryCounts`
        if (player.getInventoryNames().size() != player.getInventoryCounts().size()) {
            System.out.println("Eroare: Dimensiunile listelor inventarului nu coincid!");
            return;
        }

        for (int i = 0; i < player.getInventoryNames().size(); i++) {
            String formattedItem = player.getInventoryNames().get(i) + " (" + player.getInventoryCounts().get(i) + ")";
            formattedInventory.add(formattedItem);
        }

        inventoryList.setItems(formattedInventory); // Actualizam vizual inventarul
    }




    public void saveGame() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("save.txt"))) {
            // Salvam poziția jucatorului
            writer.write(gameInteractions.getPlayerX() + " " + gameInteractions.getPlayerY());
            writer.newLine();

            // Salvam atributele jucatorului
            writer.write(player.getHp() + " " + player.getArmor() + " " + player.getAttack());
            writer.newLine();

            // Salvam echipamentele jucatorului
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

            // Salvam harta
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[y].length; x++) {
                    writer.write((map[y][x] == null ? "null" : map[y][x].toString()) + " ");
                }
                writer.newLine();
            }

            // Salvam inventarul
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
            // Încarcam poziția jucatorului
            String[] position = reader.readLine().split(" ");
            int playerX = Integer.parseInt(position[0]);
            int playerY = Integer.parseInt(position[1]);
            gameInteractions.setPlayerPosition(playerX, playerY);

            // Încarcam atributele jucatorului
            String[] playerAttributes = reader.readLine().split(" ");
            player.setBaseHp(Integer.parseInt(playerAttributes[0]));
            player.setBaseArmor(Integer.parseInt(playerAttributes[1]));
            player.setBaseAttack(Integer.parseInt(playerAttributes[2]));

            // Încarcam echipamentele jucatorului
            String equippedLine = reader.readLine();
            if (!equippedLine.isEmpty() && !equippedLine.equals("null")) {
                String[] equippedItems = equippedLine.split(";");
                for (String equippedItem : equippedItems) {
                    if (!equippedItem.trim().isEmpty()) {
                        String[] itemData = equippedItem.split(",");
                        if (itemData.length == 5) { // Verificam formatul corect
                            String name = itemData[0];
                            String type = itemData[1];
                            int hpBonus = Integer.parseInt(itemData[2]);
                            int armorBonus = Integer.parseInt(itemData[3]);
                            int attackBonus = Integer.parseInt(itemData[4]);

                            Item item = new Item(name, type, hpBonus, armorBonus, attackBonus);
                            player.equipItem(item); // Echipam itemul
                        } else {
                            System.out.println("Eroare: Format incorect pentru echipamente -> " + equippedItem);
                        }
                    }
                }
            }

            // Încarcam harta
            for (int y = 0; y < map.length; y++) {
                String[] line = reader.readLine().split(" ");
                for (int x = 0; x < map[y].length; x++) {
                    map[y][x] = line[x].equals("null") ? null : line[x];
                }
            }

            // Încarcam inventarul
            player.getInventoryNames().clear();
            player.getInventoryCounts().clear();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    player.addItemToInventory(parts[0]); // Adaugam obiectul în inventar
                    int count = Integer.parseInt(parts[1]);
                    int index = player.getInventoryNames().indexOf(parts[0]);
                    if (index >= 0) {
                        player.getInventoryCounts().set(index, count);
                    }
                } else {
                    System.out.println("Eroare: Format incorect pentru inventar -> " + line);
                }
            }

            // Actualizam interfața
            updatePlayerStats(); // Actualizam statisticile
            updateFormattedInventory(); // Actualizam inventarul
            drawMap(); // Redesenam harta

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

        statsList.setItems(playerStats); // Actualizeaza vizual statisticile
    }




    public void unequipItem(String itemType) {
        player.getEquippedItems().removeIf(item -> item.getType().equals(itemType));
        updatePlayerStats(); // Actualizam statisticile dupa dezechipare
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

        // Ieșire din aplicație
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
