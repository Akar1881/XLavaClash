package com.xlavaclash.game;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.GameMap;
import com.xlavaclash.models.GamePlayer;
import com.xlavaclash.models.GameState;
import com.xlavaclash.models.Team;
import com.xlavaclash.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Game {
    private final XLavaClash plugin;
    private final GameMap map;
    private final Map<UUID, GamePlayer> players;
    private GameState state;
    private final long startTime;
    private final GameTimer gameTimer;

    public Game(XLavaClash plugin, GameMap map) {
        this.plugin = plugin;
        this.map = map;
        this.players = new HashMap<>();
        this.state = GameState.WAITING;
        this.startTime = System.currentTimeMillis();
        this.gameTimer = new GameTimer(plugin, this);
        
        // Initialize XP tracking for all players
        players.values().forEach(gamePlayer -> 
            plugin.getXpManager().initializeGameStats(gamePlayer.getPlayer()));
    }

    public void addPlayer(Player player, Team team) {
        PlayerUtils.resetPlayer(player);
        GamePlayer gamePlayer = new GamePlayer(player, team);
        players.put(player.getUniqueId(), gamePlayer);
        
        if (shouldStartCountdown()) {
            gameTimer.startCountdown();
        }
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        PlayerUtils.resetPlayer(player);
        teleportToLobby(player);
        
        if (state == GameState.WAITING && !hasMinimumPlayers()) {
            gameTimer.cancelCountdown();
        }
    }

    private boolean shouldStartCountdown() {
        return state == GameState.WAITING && hasMinimumPlayers();
    }

    private boolean hasMinimumPlayers() {
        return players.size() >= map.getTeamSize().getSize() * 2;
    }

    public void teleportPlayers() {
        players.values().forEach(gamePlayer -> {
            Player player = gamePlayer.getPlayer();
            Location spawn = gamePlayer.getTeam() == Team.RED ? 
                map.getRedSpawn() : map.getBlueSpawn();
            player.teleport(spawn);
        });
    }

    private void teleportToLobby(Player player) {
        Location lobby = plugin.getConfigManager().getLobbyLocation();
        if (lobby != null) {
            player.teleport(lobby);
        }
    }

    public void endGame(Team winner) {
        state = GameState.ENDING;
        gameTimer.stop();
        broadcast("§6" + winner.name() + " §eTeam wins the game!");
        
        // Calculate survival time and award XP for all players
        long gameEndTime = System.currentTimeMillis();
        players.values().forEach(gamePlayer -> {
            Player player = gamePlayer.getPlayer();
            long survivalTime = (gameEndTime - startTime) / 1000; // Convert to seconds
            plugin.getXpManager().setSurvivalTime(player, survivalTime);
            plugin.getXpManager().calculateAndAwardXp(player);
        });
        
        // Clear all players from queue
        plugin.getQueueManager().removeAllPlayers();
        
        // Teleport players to lobby
        Location globalLobby = plugin.getConfigManager().getLobbyLocation();
        if (globalLobby != null) {
            players.values().forEach(gamePlayer -> {
                Player player = gamePlayer.getPlayer();
                PlayerUtils.resetPlayer(player);
                player.teleport(globalLobby);
            });
        }
        
        players.clear();
    }

    public void broadcast(String message) {
        players.values().forEach(gamePlayer -> 
            gamePlayer.getPlayer().sendMessage(message));
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