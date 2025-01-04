package com.xlavaclash.gui;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.Team;
import com.xlavaclash.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TeamSelectionGui {
    private final XLavaClash plugin;
    private final String mapName;
    private final Inventory inventory;

    public TeamSelectionGui(XLavaClash plugin, String mapName) {
        this.plugin = plugin;
        this.mapName = mapName;
        this.inventory = Bukkit.createInventory(null, 27, "§8Select a Team");
        setupItems();
    }

    private void setupItems() {
        GuiUtils.fillEmptySlots(inventory);

        // Red Team (slot 11)
        inventory.setItem(11, createTeamItem(Team.RED));
        
        // Random Team (slot 13)
        inventory.setItem(13, createRandomTeamItem());
        
        // Blue Team (slot 15)
        inventory.setItem(15, createTeamItem(Team.BLUE));
        
        // Back button (slot 22)
        inventory.setItem(22, createBackButton());
        
        // Store map data
        GuiUtils.setMapData(inventory, mapName);
    }

    private ItemStack createTeamItem(Team team) {
        ItemStack item = new ItemStack(team == Team.RED ? Material.RED_WOOL : Material.BLUE_WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(team.getColor() + team.name() + " Team");
        
        List<String> lore = new ArrayList<>();
        int teamCount = plugin.getQueueManager().getTeamQueueCount(mapName, team);
        int maxTeamSize = plugin.getMapManager().getMap(mapName)
            .map(map -> map.getTeamSize().getSize())
            .orElse(1);
            
        lore.add("§7Players in queue: §f" + teamCount + "/" + maxTeamSize);
        lore.add("");
        lore.add("§7Click to join " + team.getColor() + team.name() + " §7team!");
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        // Store team data
        GuiUtils.setTeamData(item, team.name());
        return item;
    }

    private ItemStack createRandomTeamItem() {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§eRandom Team");
        
        List<String> lore = new ArrayList<>();
        int totalPlayers = plugin.getQueueManager().getPlayersInQueue(mapName);
        int maxPlayers = plugin.getMapManager().getMap(mapName)
            .map(map -> map.getTeamSize().getSize() * 2)
            .orElse(2);
            
        lore.add("§7Total players: §f" + totalPlayers + "/" + maxPlayers);
        lore.add("");
        lore.add("§7Click to join a random team!");
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        // Store team data
        GuiUtils.setTeamData(item, "RANDOM");
        return item;
    }

    private ItemStack createBackButton() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cBack to Map Selection");
        List<String> lore = new ArrayList<>();
        lore.add("§7Click to return to map selection");
        meta.setLore(lore);
        item.setItemMeta(meta);
        GuiUtils.setTeamData(item, "BACK");
        return item;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }
}