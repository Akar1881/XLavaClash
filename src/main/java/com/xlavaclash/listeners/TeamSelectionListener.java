package com.xlavaclash.listeners;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.Team;
import com.xlavaclash.utils.GuiUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TeamSelectionListener implements Listener {
    private final XLavaClash plugin;

    public TeamSelectionListener(XLavaClash plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getView().getTitle().equals("§8Select a Team")) return;

        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.BLACK_STAINED_GLASS_PANE) return;

        Player player = (Player) event.getWhoClicked();
        String mapName = GuiUtils.getMapData(event.getInventory());
        if (mapName == null) return;

        int slot = event.getSlot();
        switch (slot) {
            case 11: // Red Team
                handleTeamSelection(player, mapName, Team.RED);
                break;
            case 13: // Random Team
                handleRandomTeam(player, mapName);
                break;
            case 15: // Blue Team
                handleTeamSelection(player, mapName, Team.BLUE);
                break;
            case 22: // Back button
                plugin.getGuiManager().openMapSelection(player);
                break;
        }
    }

    private void handleTeamSelection(Player player, String mapName, Team team) {
        if (plugin.getQueueManager().addPlayer(player, mapName, team)) {
            player.sendMessage(team.getColor() + "You joined the " + team.name() + " team queue!");
            player.closeInventory();
        }
    }

    private void handleRandomTeam(Player player, String mapName) {
        if (plugin.getQueueManager().addPlayer(player, mapName, null)) {
            player.sendMessage("§eYou joined a random team queue!");
            player.closeInventory();
        }
    }
}