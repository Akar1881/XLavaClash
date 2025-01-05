package com.xlavaclash.listeners;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.GameState;
import com.xlavaclash.models.Team;
import com.xlavaclash.utils.PlayerDeathHandler;
import com.xlavaclash.utils.PlayerRespawnHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerListener implements Listener {
    private final XLavaClash plugin;
    private final PlayerDeathHandler deathHandler;
    private final PlayerRespawnHandler respawnHandler;

    public PlayerListener(XLavaClash plugin) {
        this.plugin = plugin;
        this.deathHandler = new PlayerDeathHandler(plugin);
        this.respawnHandler = new PlayerRespawnHandler(plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getQueueManager().removePlayer(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        deathHandler.handlePlayerDeath(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        respawnHandler.handlePlayerRespawn(event);
    }
}