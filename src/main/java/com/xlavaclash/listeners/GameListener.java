package com.xlavaclash.listeners;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.GamePlayer;
import com.xlavaclash.models.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GameListener implements Listener {
    private final XLavaClash plugin;

    public GameListener(XLavaClash plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        plugin.getGameManager().getActiveGames().values().stream()
            .filter(game -> game.getState() != GameState.PLAYING)
            .filter(game -> game.getPlayers().stream()
                .anyMatch(gp -> gp.getPlayer().equals(player)))
            .findFirst()
            .ifPresent(game -> event.setCancelled(true));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        plugin.getGameManager().getActiveGames().values().stream()
            .filter(game -> game.getState() != GameState.PLAYING)
            .filter(game -> game.getPlayers().stream()
                .anyMatch(gp -> gp.getPlayer().equals(player)))
            .findFirst()
            .ifPresent(game -> event.setCancelled(true));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            plugin.getXpManager().addKill(killer);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;
        
        Player damager = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();
        
        plugin.getGameManager().getActiveGames().values().stream()
            .filter(game -> game.getPlayers().stream()
                .anyMatch(gp -> gp.getPlayer().equals(damager) || gp.getPlayer().equals(victim)))
            .findFirst()
            .ifPresent(game -> {
                if (game.getState() != GameState.PLAYING) {
                    event.setCancelled(true);
                    return;
                }
                
                GamePlayer damagerPlayer = game.getPlayers().stream()
                    .filter(gp -> gp.getPlayer().equals(damager))
                    .findFirst()
                    .orElse(null);
                    
                GamePlayer victimPlayer = game.getPlayers().stream()
                    .filter(gp -> gp.getPlayer().equals(victim))
                    .findFirst()
                    .orElse(null);
                    
                if (damagerPlayer != null && victimPlayer != null) {
                    if (damagerPlayer.getTeam() == victimPlayer.getTeam()) {
                        event.setCancelled(true);
                    } else {
                        // Track damage for XP
                        plugin.getXpManager().addDamage(damager, (int) event.getFinalDamage());
                    }
                }
            });
    }
}