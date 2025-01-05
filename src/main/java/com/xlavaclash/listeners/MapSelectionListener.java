package com.xlavaclash.listeners;

import com.xlavaclash.XLavaClash;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MapSelectionListener implements Listener {
    private final XLavaClash plugin;

    public MapSelectionListener(XLavaClash plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getView().getTitle().equals("§8Select a Map")) return;

        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.BLACK_STAINED_GLASS_PANE) return;

        int startSlot = plugin.getConfig().getInt("gui.map-selection.start-slot", 9);
        int endSlot = plugin.getConfig().getInt("gui.map-selection.end-slot", 17);
        
        // Only handle configured slot range
        int slot = event.getSlot();
        if (slot < startSlot || slot > endSlot) return;

        Player player = (Player) event.getWhoClicked();
        String mapName = plugin.getMapManager().getActiveMaps().keySet().stream()
            .skip(slot - startSlot)
            .findFirst()
            .orElse(null);

        if (mapName != null) {
            if (plugin.getGameManager().hasActiveGame(mapName)) {
                player.sendMessage("§cThis map currently has an active game!");
                return;
            }
            plugin.getGuiManager().openTeamSelection(player, mapName);
        }
    }
}