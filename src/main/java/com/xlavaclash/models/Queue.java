package com.xlavaclash.models;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.game.Game;
import com.xlavaclash.utils.QueueItems;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.*;

public class Queue {
    private final XLavaClash plugin;
    private final GameMap map;
    private final Map<UUID, Team> players;
    private final Random random;
    private boolean starting;

    public Queue(XLavaClash plugin, GameMap map) {
        this.plugin = plugin;
        this.map = map;
        this.players = new HashMap<>();
        this.random = new Random();
        this.starting = false;
    }

    public void clear() {
        players.keySet().stream()
            .map(plugin.getServer()::getPlayer)
            .filter(Objects::nonNull)
            .forEach(player -> {
                plugin.getInventoryManager().restoreInventory(player);
                Location lobby = plugin.getConfigManager().getLobbyLocation();
                if (lobby != null) {
                    player.teleport(lobby);
                }
            });
        players.clear();
    }

    public boolean addPlayer(Player player) {
        return addPlayer(player, getBalancedTeam());
    }

    public boolean addPlayer(Player player, Team team) {
        if (players.containsKey(player.getUniqueId()) || starting) {
            return false;
        }

        players.put(player.getUniqueId(), team);
        plugin.getInventoryManager().saveInventory(player);
        
        // Give leave item
        player.getInventory().setItem(8, QueueItems.createLeaveItem(map.getName()));
        
        // Teleport to queue lobby
        if (map.getQueueLobby() != null) {
            player.teleport(map.getQueueLobby());
        }
        
        broadcastQueueUpdate();
        checkQueue();
        
        return true;
    }

    public void removePlayer(Player player) {
        if (players.remove(player.getUniqueId()) != null) {
            plugin.getInventoryManager().restoreInventory(player);
            player.getInventory().remove(QueueItems.createLeaveItem(map.getName()));
            broadcastQueueUpdate();
        }
    }

    private Team getBalancedTeam() {
        long redCount = players.values().stream().filter(t -> t == Team.RED).count();
        long blueCount = players.values().stream().filter(t -> t == Team.BLUE).count();

        if (redCount < blueCount) {
            return Team.RED;
        } else if (blueCount < redCount) {
            return Team.BLUE;
        } else {
            return random.nextBoolean() ? Team.RED : Team.BLUE;
        }
    }

    private void broadcastQueueUpdate() {
        int current = players.size();
        int required = map.getTeamSize().getSize() * 2;
        String message = String.format("§eQueue: §f%d/%d players", current, required);
        
        players.keySet().stream()
            .map(plugin.getServer()::getPlayer)
            .filter(Objects::nonNull)
            .forEach(p -> p.sendMessage(message));
    }

    private void checkQueue() {
        if (starting) return;
        
        int required = map.getTeamSize().getSize() * 2;
        if (players.size() >= required) {
            starting = true;
            startGame();
        }
    }

    private void startGame() {
        Game game = plugin.getGameManager().createGame(map);
        
        players.forEach((uuid, team) -> {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null) {
                game.addPlayer(player, team);
            }
        });
        
        players.clear();
    }

    public GameMap getMap() {
        return map;
    }

    public Map<UUID, Team> getPlayers() {
        return players;
    }
}