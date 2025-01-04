package com.xlavaclash.listeners;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.Team;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final XLavaClash plugin;

    public PlayerListener(XLavaClash plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getQueueManager().removePlayer(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        plugin.getGameManager().getActiveGames().values().stream()
            .filter(game -> game.getPlayers().stream()
                .anyMatch(gp -> gp.getPlayer().equals(player)))
            .findFirst()
            .ifPresent(game -> {
                // Cancel drops and exp
                event.setDroppedExp(0);
                event.getDrops().clear();
                
                // Set player to spectator mode
                player.setGameMode(GameMode.SPECTATOR);
                
                // Mark player as not alive in game
                game.getPlayers().stream()
                    .filter(gp -> gp.getPlayer().equals(player))
                    .findFirst()
                    .ifPresent(gp -> gp.setAlive(false));
                
                // Check if team has any players left
                Team playerTeam = game.getPlayers().stream()
                    .filter(gp -> gp.getPlayer().equals(player))
                    .findFirst()
                    .get()
                    .getTeam();

                long remainingTeamPlayers = game.getPlayers().stream()
                    .filter(gp -> gp.getTeam() == playerTeam && gp.isAlive())
                    .count();

                if (remainingTeamPlayers == 0) {
                    // End game if no players left on team
                    Team winner = (playerTeam == Team.RED) ? Team.BLUE : Team.RED;
                    game.endGame(winner);
                }
            });
    }
}