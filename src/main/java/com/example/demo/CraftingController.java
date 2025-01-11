package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class CraftingController {

    @FXML
    private ListView<String> craftingOptions;

    private GameIntegration gameIntegration;

    public void setGameIntegration(GameIntegration gameIntegration) {
        this.gameIntegration = gameIntegration;
        initializeCraftingOptions();
    }

    private void initializeCraftingOptions() {
        ObservableList<String> options = FXCollections.observableArrayList(
            "Casca Zeului",
            "Sabia Zeului",
            "Armura de Zeu"
        );
        craftingOptions.setItems(options);
    }

    @FXML
    private void handleCraft() {
        String selectedOption = craftingOptions.getSelectionModel().getSelectedItem();
        if (selectedOption == null) {
            showAlert("Selecteaza un obiect!", "Trebuie sa selectezi un obiect pentru a-l crafta.");
            return;
        }

        boolean success = false;
        switch (selectedOption) {
            case "Casca Zeului":
                success = craftItem("Casca a Inamicului", "Casca Zeului", "Casca", 10, 5, 0);
                break;
            case "Sabia Zeului":
                success = craftItem("Sabie a Inamicului", "Sabia Zeului", "Arma", 0, 0, 20);
                break;
            case "Armura de Zeu":
                success = craftItem("Armura a Inamicului", "Armura de Zeu", "Armura", 20, 10, 0);
                break;
        }

        if (success) {
            closeWindow();
        }
    }

    private boolean craftItem(String baseItemName, String newItemName, String type, int hpBonus, int armorBonus, int attackBonus) {
        // Debugging pentru inventar
        System.out.println("Inventar curent: " + gameIntegration.getPlayer().getInventoryNames());
        System.out.println("Cantitați: " + gameIntegration.getPlayer().getInventoryCounts());

        int bloodCount = gameIntegration.getPlayer().getItemCount("Sângele Inamicului");
        int baseItemCount = gameIntegration.getPlayer().getItemCount(baseItemName);

        // Verificam daca jucatorul are suficiente materiale
        if (bloodCount < 5) {
            showAlert("Materiale insuficiente!", "Ai nevoie de 5 Sânge de Inamic pentru a crea " + newItemName + ".");
            return false;
        }

        if (baseItemCount < 1) {
            showAlert("Materiale insuficiente!", "Ai nevoie de " + baseItemName + " pentru a crea " + newItemName + ".");
            return false;
        }

        // Consumam materialele necesare
        for (int i = 0; i < 5; i++) {
            gameIntegration.getPlayer().useItem("Sângele Inamicului");
        }
        gameIntegration.getPlayer().useItem(baseItemName);

        // Cream și echipam noul item
        Item newItem = new Item(newItemName, type, hpBonus, armorBonus, attackBonus);
        gameIntegration.getPlayer().equipItem(newItem);

        // Actualizam interfața
        gameIntegration.updatePlayerStats();
        gameIntegration.updateFormattedInventory();
        showAlert("Succes!", newItemName + " a fost creat și echipat cu succes.");
        return true;
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) craftingOptions.getScene().getWindow();
        stage.close();
    }
}
