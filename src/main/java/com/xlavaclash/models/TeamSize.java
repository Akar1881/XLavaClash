package com.xlavaclash.models;

public enum TeamSize {
    SOLO(1),
    DUO(2),
    TRIO(3),
    SQUAD(4);

    private final int size;

    TeamSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}