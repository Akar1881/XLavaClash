package com.xlavaclash.gui;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.GameMap;
import com.xlavaclash.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MapSelectionGui {
    private final XLavaClash plugin;
    private final Inventory inventory;
    private final int startSlot;
    private final int endSlot;

    public MapSelectionGui(XLavaClash plugin) {
        this.plugin = plugin;
        int size = plugin.getConfig().getInt("gui.map-selection.size", 27);
        this.startSlot = plugin.getConfig().getInt("gui.map-selection.start-slot", 9);
        this.endSlot = plugin.getConfig().getInt("gui.map-selection.end-slot", 17);
        
        // Ensure size is multiple of 9
        size = Math.max(9, (size / 9) * 9);
        this.inventory = Bukkit.createInventory(null, size, "§8Select a Map");
        setupItems();
    }

    private void setupItems() {
        GuiUtils.fillEmptySlots(inventory);

        int slot = startSlot;
        
        for (GameMap map : plugin.getMapManager().getActiveMaps().values()) {
            if (slot > endSlot) break;

            ItemStack item = new ItemStack(Material.MAP);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§6" + map.getName());
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Team Size: §f" + map.getTeamSize().name());
            
            // Add queue information
            int playersInQueue = plugin.getQueueManager().getPlayersInQueue(map.getName());
            int maxPlayers = map.getTeamSize().getSize() * 2;
            lore.add("§7Queue: §f" + playersInQueue + "/" + maxPlayers);
            
            // Add active game information
            boolean hasActiveGame = plugin.getGameManager().hasActiveGame(map.getName());
            if (hasActiveGame) {
                lore.add("§cGame in progress!");
            }
            
            lore.add("");
            if (!hasActiveGame) {
                lore.add("§eClick to join!");
            } else {
                lore.add("§cWait for current game to end");
            }
            
            meta.setLore(lore);
            item.setItemMeta(meta);
            
            GuiUtils.setMapData(item, map.getName());
            inventory.setItem(slot, item);
            slot++;
        }
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public static int getStartSlot(XLavaClash plugin) {
        return plugin.getConfig().getInt("gui.map-selection.start-slot", 9);
    }

    public static int getEndSlot(XLavaClash plugin) {
        return plugin.getConfig().getInt("gui.map-selection.end-slot", 17);
    }
}