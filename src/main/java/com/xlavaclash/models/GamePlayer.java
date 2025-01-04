package com.xlavaclash.models;

import org.bukkit.entity.Player;

public class GamePlayer {
    private final Player player;
    private final Team team;
    private boolean alive;

    public GamePlayer(Player player, Team team) {
        this.player = player;
        this.team = team;
        this.alive = true;
    }

    public Player getPlayer() {
        return player;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}