package com.xlavaclash.utils;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDeathHandler {
    private final XLavaClash plugin;

    public PlayerDeathHandler(XLavaClash plugin) {
        this.plugin = plugin;
    }

    public void handlePlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        plugin.getGameManager().getActiveGames().values().stream()
            .filter(game -> game.getPlayers().stream()
                .anyMatch(gp -> gp.getPlayer().equals(player)))
            .findFirst()
            .ifPresent(game -> {
                // Cancel drops and exp
                event.setDroppedExp(0);
                event.getDrops().clear();
                
                // Mark player as not alive in game
                game.getPlayers().stream()
                    .filter(gp -> gp.getPlayer().equals(player))
                    .findFirst()
                    .ifPresent(gp -> gp.setAlive(false));
                
                // Force respawn player immediately
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.isDead()) {
                            player.spigot().respawn();
                        }
                    }
                }.runTaskLater(plugin, 1L);
                
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