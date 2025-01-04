package com.xlavaclash.listeners;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.gui.PlayerStatusGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class PlayerStatusGuiListener implements Listener {
    private final XLavaClash plugin;

    public PlayerStatusGuiListener(XLavaClash plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("§8Your Statistics")) return;
        
        event.setCancelled(true);
        
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        
        if (event.getSlot() == 15 && event.getCurrentItem() != null && 
            event.getCurrentItem().getType() == Material.BOOK) {
            new PlayerStatusGui(plugin, player).showRankings(player);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTitle().equals("§8Your Statistics")) {
            event.setCancelled(true);
        }
    }
}