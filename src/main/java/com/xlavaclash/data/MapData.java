package com.xlavaclash.data;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.GameMap;
import com.xlavaclash.models.TeamSize;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class MapData {
    private final XLavaClash plugin;
    private final GameMap map;
    private final YamlConfiguration config;

    public MapData(XLavaClash plugin, GameMap map, YamlConfiguration config) {
        this.plugin = plugin;
        this.map = map;
        this.config = config;
    }

    public void save() {
        config.set("name", map.getName());
        config.set("world", map.getWorldName());
        config.set("active", map.isActive());
        config.set("teamSize", map.getTeamSize().name());
        
        saveLocation("queueLobby", map.getQueueLobby());
        saveLocation("buttonLocation", map.getButtonLocation());
        saveLocation("redSpawn", map.getRedSpawn());
        saveLocation("blueSpawn", map.getBlueSpawn());
    }

    public void load() {
        map.setActive(config.getBoolean("active", false));
        map.setTeamSize(TeamSize.valueOf(config.getString("teamSize", "SOLO")));
        
        map.setQueueLobby(loadLocation("queueLobby"));
        map.setButtonLocation(loadLocation("buttonLocation"));
        map.setRedSpawn(loadLocation("redSpawn"));
        map.setBlueSpawn(loadLocation("blueSpawn"));
    }

    private void saveLocation(String path, Location location) {
        if (location != null) {
            config.set(path + ".world", location.getWorld().getName());
            config.set(path + ".x", location.getX());
            config.set(path + ".y", location.getY());
            config.set(path + ".z", location.getZ());
            config.set(path + ".yaw", location.getYaw());
            config.set(path + ".pitch", location.getPitch());
        }
    }

    private Location loadLocation(String path) {
        if (!config.contains(path + ".world")) return null;
        
        return new Location(
            plugin.getServer().getWorld(config.getString(path + ".world")),
            config.getDouble(path + ".x"),
            config.getDouble(path + ".y"),
            config.getDouble(path + ".z"),
            (float) config.getDouble(path + ".yaw"),
            (float) config.getDouble(path + ".pitch")
        );
    }
}