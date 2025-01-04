package com.xlavaclash.items;

public enum ItemRarity {
    COMMON("§7", 60),
    RARE("§9", 30),
    EPIC("§5", 10);

    private final String color;
    private final int chance;

    ItemRarity(String color, int chance) {
        this.color = color;
        this.chance = chance;
    }

    public String getColor() {
        return color;
    }

    public int getChance() {
        return chance;
    }
}