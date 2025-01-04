package com.xlavaclash.models;

import org.bukkit.Location;

public class GameMap {
    private final String name;
    private final String worldName;
    private Location queueLobby;
    private Location buttonLocation;
    private Location redSpawn;
    private Location blueSpawn;
    private TeamSize teamSize;
    private boolean active;

    public GameMap(String name, String worldName) {
        this.name = name;
        this.worldName = worldName;
        this.active = false;
        this.teamSize = TeamSize.SOLO;
    }

    public String getName() {
        return name;
    }

    public String getWorldName() {
        return worldName;
    }

    public Location getQueueLobby() {
        return queueLobby;
    }

    public void setQueueLobby(Location queueLobby) {
        this.queueLobby = queueLobby;
    }

    public Location getButtonLocation() {
        return buttonLocation;
    }

    public void setButtonLocation(Location buttonLocation) {
        this.buttonLocation = buttonLocation;
    }

    public Location getRedSpawn() {
        return redSpawn;
    }

    public void setRedSpawn(Location redSpawn) {
        this.redSpawn = redSpawn;
    }

    public Location getBlueSpawn() {
        return blueSpawn;
    }

    public void setBlueSpawn(Location blueSpawn) {
        this.blueSpawn = blueSpawn;
    }

    public TeamSize getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(TeamSize teamSize) {
        this.teamSize = teamSize;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isConfigured() {
        return queueLobby != null && 
               buttonLocation != null && 
               redSpawn != null && 
               blueSpawn != null;
    }
}