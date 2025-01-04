package com.xlavaclash.models;

public enum Team {
    RED("§c"),
    BLUE("§9");

    private final String color;

    Team(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}