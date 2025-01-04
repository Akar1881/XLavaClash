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

        // Only handle slots 9-17
        int slot = event.getSlot();
        if (slot < 9 || slot > 17) return;

        Player player = (Player) event.getWhoClicked();
        String mapName = plugin.getMapManager().getActiveMaps().keySet().stream()
            .skip(slot - 9)
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