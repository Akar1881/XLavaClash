package com.xlavaclash.listeners;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.gui.PlayerStatusGui;
import com.xlavaclash.utils.LobbyItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class LobbyItemListener implements Listener {
    private final XLavaClash plugin;

    public LobbyItemListener(XLavaClash plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        checkAndGiveLobbyItems(event.getPlayer());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        checkAndGiveLobbyItems(event.getPlayer());
    }

    private void checkAndGiveLobbyItems(Player player) {
        String lobbyWorld = plugin.getConfigManager().getLobbyWorld();
        if (lobbyWorld != null && player.getWorld().getName().equals(lobbyWorld)) {
            LobbyItems.giveLobbyItems(player);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && 
            event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (LobbyItems.isMapSelector(item)) {
            event.setCancelled(true);
            plugin.getGuiManager().openMapSelection(player);
        } else if (LobbyItems.isPlayerHead(item)) {
            event.setCancelled(true);
            new PlayerStatusGui(plugin, player).open();
        }
    }
}