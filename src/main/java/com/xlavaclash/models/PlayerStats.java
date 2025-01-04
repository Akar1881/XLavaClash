package com.xlavaclash.models;

public class PlayerStats {
    private int kills;
    private int damageDealt;
    private long survivalTime;
    private boolean participated;
    private int wins;
    private int losses;

    public PlayerStats() {
        this.kills = 0;
        this.damageDealt = 0;
        this.survivalTime = 0;
        this.participated = true;
        this.wins = 0;
        this.losses = 0;
    }

    public void addKill() {
        this.kills++;
    }

    public void addDamage(int damage) {
        this.damageDealt += damage;
    }

    public void setSurvivalTime(long survivalTime) {
        this.survivalTime = survivalTime;
    }

    public void addWin() {
        this.wins++;
    }

    public void addLoss() {
        this.losses++;
    }

    public int getKills() {
        return kills;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

    public long getSurvivalTime() {
        return survivalTime;
    }

    public boolean hasParticipated() {
        return participated;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }
}