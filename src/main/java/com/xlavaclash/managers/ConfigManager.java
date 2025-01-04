package com.xlavaclash.managers;

import com.xlavaclash.XLavaClash;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final XLavaClash plugin;
    private FileConfiguration config;

    public ConfigManager(XLavaClash plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        plugin.saveConfig();
    }

    public Location getLobbyLocation() {
        if (!config.contains("lobby.world")) return null;
        
        return new Location(
            plugin.getServer().getWorld(config.getString("lobby.world")),
            config.getDouble("lobby.x"),
            config.getDouble("lobby.y"),
            config.getDouble("lobby.z"),
            (float) config.getDouble("lobby.yaw"),
            (float) config.getDouble("lobby.pitch")
        );
    }

    public String getLobbyWorld() {
        return config.getString("lobby.world");
    }
}