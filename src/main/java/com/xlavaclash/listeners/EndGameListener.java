package com.xlavaclash.listeners;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.GameState;
import com.xlavaclash.models.Team;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EndGameListener implements Listener {
    private final XLavaClash plugin;

    public EndGameListener(XLavaClash plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getGameManager().getActiveGames().values().stream()
            .filter(game -> game.getState() == GameState.PLAYING)
            .filter(game -> game.getPlayers().stream()
                .anyMatch(gp -> gp.getPlayer().equals(event.getPlayer())))
            .findFirst()
            .ifPresent(game -> {
                // Check if the leaving player's team would have no players left
                Team playerTeam = game.getPlayers().stream()
                    .filter(gp -> gp.getPlayer().equals(event.getPlayer()))
                    .findFirst()
                    .get()
                    .getTeam();

                long remainingTeamPlayers = game.getPlayers().stream()
                    .filter(gp -> gp.getTeam() == playerTeam && !gp.getPlayer().equals(event.getPlayer()))
                    .count();

                if (remainingTeamPlayers == 0) {
                    // End the game with the opposite team as winner
                    Team winner = (playerTeam == Team.RED) ? Team.BLUE : Team.RED;
                    game.endGame(winner);
                }
            });
    }
}