package com.example.demo;

public class Item {
    private String name; // Numele itemului
    private String type; // Tipul itemului: Casca, Armura, Arma
    private int hpBonus;
    private int armorBonus;
    private int attackBonus;

    public Item(String name, String type, int hpBonus, int armorBonus, int attackBonus) {
        this.name = name;
        this.type = type;
        this.hpBonus = hpBonus;
        this.armorBonus = armorBonus;
        this.attackBonus = attackBonus;
    }

    // Getteri pentru atribute
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getHpBonus() {
        return hpBonus;
    }

    public int getArmorBonus() {
        return armorBonus;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
