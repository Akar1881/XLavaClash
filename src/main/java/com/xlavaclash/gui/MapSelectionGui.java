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

    public MapSelectionGui(XLavaClash plugin) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(null, 27, "§8Select a Map");
        setupItems();
    }

    private void setupItems() {
        GuiUtils.fillEmptySlots(inventory);

        int slot = 9; // Start at slot 9
        
        for (GameMap map : plugin.getMapManager().getActiveMaps().values()) {
            if (slot > 17) break; // Only use slots 9-17

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
}