package com.xlavaclash.listeners;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.utils.QueueItems;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class QueueItemListener implements Listener {
    private final XLavaClash plugin;

    public QueueItemListener(XLavaClash plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && 
            event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (QueueItems.isLeaveItem(item)) {
            event.setCancelled(true);
            plugin.getQueueManager().removePlayer(player);
            
            Location lobbyLocation = plugin.getConfigManager().getLobbyLocation();
            if (lobbyLocation != null) {
                player.teleport(lobbyLocation);
            }
            
            player.getInventory().remove(item);
            player.sendMessage("§aYou left the queue and returned to the lobby!");
        }
    }
}