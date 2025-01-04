package com.xlavaclash.models;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Game {
    private final XLavaClash plugin;
    private final GameMap map;
    private final Map<UUID, GamePlayer> players;
    private GameState state;

    public Game(XLavaClash plugin, GameMap map) {
        this.plugin = plugin;
        this.map = map;
        this.players = new HashMap<>();
        this.state = GameState.WAITING;
    }

    public void addPlayer(Player player, Team team) {
        GamePlayer gamePlayer = new GamePlayer(player, team);
        players.put(player.getUniqueId(), gamePlayer);
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
    }

    public void broadcast(String message) {
        players.values().forEach(gamePlayer -> 
            gamePlayer.getPlayer().sendMessage(message));
    }

    public void endGame(Team winner) {
        state = GameState.ENDING;
        broadcast("§6" + winner.name() + " §eTeam wins the game!");
        
        // Clear all players from queue
        plugin.getQueueManager().removeAllPlayers();
        
        // Get global lobby location
        Location globalLobby = plugin.getConfigManager().getLobbyLocation();
        if (globalLobby != null) {
            // Teleport all players to lobby and reset them
            players.values().forEach(gamePlayer -> {
                Player player = gamePlayer.getPlayer();
                PlayerUtils.resetPlayer(player);
                player.getInventory().clear();
                player.teleport(globalLobby);
            });
        }
        
        players.clear();
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GameMap getMap() {
        return map;
    }

    public Collection<GamePlayer> getPlayers() {
        return players.values();
    }
}