package com.xlavaclash.listeners;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.Team;
import com.xlavaclash.utils.GuiUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {
    private final XLavaClash plugin;

    public GuiListener(XLavaClash plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        
        String title = event.getView().getTitle();
        if (!title.equals("§8Select a Map") && !title.equals("§8Select a Team")) return;
        
        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.BLACK_STAINED_GLASS_PANE) return;

        String mapName = GuiUtils.getMapData(event.getInventory());
        if (mapName == null) return;

        if (title.equals("§8Select a Map")) {
            if (event.getSlot() >= 9 && event.getSlot() <= 17) { // Only slots 9-17
                handleMapSelection(player, mapName);
            }
        } else if (title.equals("§8Select a Team")) {
            String teamData = GuiUtils.getTeamData(clicked);
            if (teamData != null) {
                handleTeamSelection(player, mapName, teamData);
            }
        }
    }

    private void handleMapSelection(Player player, String mapName) {
        if (plugin.getGameManager().hasActiveGame(mapName)) {
            player.sendMessage("§cThis map currently has an active game!");
            return;
        }
        plugin.getGuiManager().openTeamSelection(player, mapName);
    }

    private void handleTeamSelection(Player player, String mapName, String teamData) {
        switch (teamData) {
            case "RANDOM":
                if (plugin.getQueueManager().addPlayer(player, mapName, null)) {
                    player.sendMessage("§eYou joined a random team queue!");
                    player.closeInventory();
                }
                break;
            case "BACK":
                plugin.getGuiManager().openMapSelection(player);
                break;
            default:
                try {
                    Team team = Team.valueOf(teamData);
                    if (plugin.getQueueManager().addPlayer(player, mapName, team)) {
                        player.sendMessage(team.getColor() + "You joined the " + team.name() + " team queue!");
                        player.closeInventory();
                    }
                } catch (IllegalArgumentException e) {
                    player.sendMessage("§cInvalid team selection!");
                }
                break;
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        String title = event.getView().getTitle();
        if (title.equals("§8Select a Map") || title.equals("§8Select a Team")) {
            event.setCancelled(true);
        }
    }
}