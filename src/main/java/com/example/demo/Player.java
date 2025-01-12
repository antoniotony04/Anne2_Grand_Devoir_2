package com.example.demo;

import java.util.ArrayList;

public class Player {
    private int baseHp;
    private int baseArmor;
    private int baseAttack;
    private ArrayList<Item> equippedItems;

    
    private ArrayList<String> inventoryNames = new ArrayList<>();
    private ArrayList<Integer> inventoryCounts = new ArrayList<>();

    public Player() {
        this.baseHp = 100;
        this.baseArmor = 0;
        this.baseAttack = 10;
        this.equippedItems = new ArrayList<>();
    }

    
    public int getHp() {
        int bonusHp = equippedItems.stream().mapToInt(Item::getHpBonus).sum();
        return baseHp + bonusHp;
    }

    public int getArmor() {
        int bonusArmor = equippedItems.stream().mapToInt(Item::getArmorBonus).sum();
        return baseArmor + bonusArmor;
    }

    public int getAttack() {
        int bonusAttack = equippedItems.stream().mapToInt(Item::getAttackBonus).sum();
        return baseAttack + bonusAttack;
    }

    public void takeDamage(int damage) {
        baseHp -= Math.max(0, damage - getArmor());
    }

    
    public void equipItem(Item item) {
        
        equippedItems.removeIf(equipped -> equipped.getType().equals(item.getType()));
        equippedItems.add(item);

        
        int index = inventoryNames.indexOf(item.getName());
        if (index >= 0) {
            inventoryCounts.set(index, inventoryCounts.get(index) - 1);
            if (inventoryCounts.get(index) <= 0) {
                inventoryNames.remove(index);
                inventoryCounts.remove(index);
            }
        }
    }





    public ArrayList<Item> getEquippedItems() {
        return equippedItems;
    }

    public void setBaseHp(int baseHp) {
        this.baseHp = baseHp;
    }

    public void setBaseArmor(int baseArmor) {
        this.baseArmor = baseArmor;
    }

    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }

    public void setEquippedItems(ArrayList<Item> equippedItems) {
        this.equippedItems = equippedItems;
    }

    
    public ArrayList<String> getInventoryNames() {
        return inventoryNames;
    }

    public ArrayList<Integer> getInventoryCounts() {
        return inventoryCounts;
    }

    public void addItemToInventory(String itemName) {
        int index = inventoryNames.indexOf(itemName);
        if (index >= 0) {
            
            inventoryCounts.set(index, inventoryCounts.get(index) + 1);
        } else {
            
            inventoryNames.add(itemName);
            inventoryCounts.add(1);
        }
    }

    public void useItem(String itemName) {
        int index = inventoryNames.indexOf(itemName);
        if (index >= 0) {
            int count = inventoryCounts.get(index);
            if (count > 0) {
                inventoryCounts.set(index, count - 1);
                if (inventoryCounts.get(index) == 0) {
                    inventoryNames.remove(index);
                    inventoryCounts.remove(index);
                }
            }
        }
    }

    public boolean hasItem(String itemName) {
        int index = inventoryNames.indexOf(itemName);
        return index >= 0 && inventoryCounts.get(index) > 0;
    }

    public int getItemCount(String itemName) {
        int index = inventoryNames.indexOf(itemName.trim()); 
        return index >= 0 ? inventoryCounts.get(index) : 0;
    }


}
