package com.xlavaclash.managers;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.data.MapStorage;
import com.xlavaclash.models.GameMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MapManager {
    private final XLavaClash plugin;
    private final MapStorage storage;
    private final Map<String, GameMap> maps;

    public MapManager(XLavaClash plugin) {
        this.plugin = plugin;
        this.storage = new MapStorage(plugin);
        this.maps = new HashMap<>();
        loadMaps();
    }

    private void loadMaps() {
        storage.loadAllMaps().forEach(map -> maps.put(map.getName(), map));
    }

    public boolean createMap(String mapName, String worldName) {
        if (maps.containsKey(mapName)) {
            return false;
        }
        GameMap map = new GameMap(mapName, worldName);
        maps.put(mapName, map);
        storage.saveMap(map);
        return true;
    }

    public boolean deleteMap(String mapName) {
        if (maps.remove(mapName) != null) {
            storage.deleteMap(mapName);
            return true;
        }
        return false;
    }

    public Optional<GameMap> getMap(String mapName) {
        return Optional.ofNullable(maps.get(mapName));
    }

    public Map<String, GameMap> getActiveMaps() {
        Map<String, GameMap> activeMaps = new HashMap<>();
        maps.forEach((name, map) -> {
            if (map.isActive()) {
                activeMaps.put(name, map);
            }
        });
        return activeMaps;
    }

    public Map<String, GameMap> getAllMaps() {
        return new HashMap<>(maps);
    }

    public void saveMap(GameMap map) {
        storage.saveMap(map);
    }
}