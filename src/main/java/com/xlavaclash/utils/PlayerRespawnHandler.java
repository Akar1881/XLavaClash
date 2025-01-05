package com.xlavaclash.utils;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.GameState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerRespawnHandler {
    private final XLavaClash plugin;

    public PlayerRespawnHandler(XLavaClash plugin) {
        this.plugin = plugin;
    }

    public void handlePlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        plugin.getGameManager().getActiveGames().values().stream()
            .filter(game -> game.getPlayers().stream()
                .anyMatch(gp -> gp.getPlayer().equals(player)))
            .findFirst()
            .ifPresent(game -> {
                // Set respawn location to death location first
                Location deathLoc = player.getLocation();
                event.setRespawnLocation(deathLoc);
                
                // Then handle spectator mode and teleport after a short delay
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.setGameMode(GameMode.SPECTATOR);
                        
                        // If game is ending, teleport to lobby
                        if (game.getState() == GameState.ENDING) {
                            Location lobbyLoc = plugin.getConfigManager().getLobbyLocation();
                            if (lobbyLoc != null) {
                                player.teleport(lobbyLoc);
                            }
                        }
                    }
                }.runTaskLater(plugin, 1L);
            });
    }
}