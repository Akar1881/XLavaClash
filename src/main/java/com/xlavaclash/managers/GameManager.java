package com.xlavaclash.managers;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.game.Game;
import com.xlavaclash.models.GameMap;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private final XLavaClash plugin;
    private final Map<String, Game> activeGames;

    public GameManager(XLavaClash plugin) {
        this.plugin = plugin;
        this.activeGames = new HashMap<>();
    }

    public Game createGame(GameMap map) {
        Game game = new Game(plugin, map);
        activeGames.put(map.getName(), game);
        return game;
    }

    public boolean hasActiveGame(String mapName) {
        return activeGames.containsKey(mapName);
    }

    public void stopGame(String mapName) {
        Game game = activeGames.remove(mapName);
        if (game != null) {
            game.broadcast("§cGame stopped by admin!");
        }
    }

    public void stopAllGames() {
        activeGames.values().forEach(game -> 
            game.broadcast("§cAll games stopped by admin!"));
        activeGames.clear();
    }

    public Game getGame(String mapName) {
        return activeGames.get(mapName);
    }

    public Map<String, Game> getActiveGames() {
        return activeGames;
    }
}