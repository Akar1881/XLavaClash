package com.xlavaclash.data;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.GameMap;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapStorage {
    private final XLavaClash plugin;
    private final File mapsFolder;

    public MapStorage(XLavaClash plugin) {
        this.plugin = plugin;
        this.mapsFolder = new File(plugin.getDataFolder(), "maps");
        if (!mapsFolder.exists()) {
            mapsFolder.mkdirs();
        }
    }

    public void saveMap(GameMap map) {
        File file = new File(mapsFolder, map.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        MapData data = new MapData(plugin, map, config);
        data.save();
        
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save map: " + map.getName());
            e.printStackTrace();
        }
    }

    public GameMap loadMap(String name) {
        File file = new File(mapsFolder, name + ".yml");
        if (!file.exists()) return null;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String worldName = config.getString("world");
        
        GameMap map = new GameMap(name, worldName);
        MapData data = new MapData(plugin, map, config);
        data.load();
        
        return map;
    }

    public List<GameMap> loadAllMaps() {
        List<GameMap> maps = new ArrayList<>();
        File[] files = mapsFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        
        if (files != null) {
            for (File file : files) {
                String mapName = file.getName().replace(".yml", "");
                GameMap map = loadMap(mapName);
                if (map != null) {
                    maps.add(map);
                }
            }
        }
        
        return maps;
    }

    public void deleteMap(String name) {
        File file = new File(mapsFolder, name + ".yml");
        if (file.exists()) {
            file.delete();
        }
    }
}